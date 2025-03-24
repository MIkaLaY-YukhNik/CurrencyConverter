package com.example.currency_converter.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class CurrencyService {

    public boolean isCurrencyValid(String currencyCode) {
        Map<String, Double> rates = fetchRates();
        return rates.containsKey(currencyCode.toUpperCase());
    }

    public double convertFromUSD(double amount, String targetCurrency) {
        Map<String, Double> rates = fetchRates();

        if (!rates.containsKey(targetCurrency.toUpperCase())) {
            throw new IllegalArgumentException("Валюта " + targetCurrency + " не поддерживается.");
        }

        double rate = rates.get(targetCurrency.toUpperCase());
        return amount * rate;
    }

    private String getApiKey() {
        Properties properties = new Properties();
        try {
            properties.load(new ClassPathResource("application.properties").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить файл конфигурации.", e);
        }
        return properties.getProperty("openexchange.api.key");
    }

    private Map<String, Double> fetchRates() {
        String apiKey = getApiKey();
        String apiUrl = "https://openexchangerates.org/api/latest.json?app_id=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
            Map<String, Object> rawRates = (Map<String, Object>) response.get("rates");

            Map<String, Double> rates = new HashMap<>();
            rawRates.forEach((key, value) -> {
                if (value instanceof Integer) {
                    rates.put(key, ((Integer) value).doubleValue());
                } else if (value instanceof Double) {
                    rates.put(key, (Double) value);
                }
            });

            return rates;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении данных от API: " + e.getMessage());
        }
    }
}
