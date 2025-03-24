package com.example.currency_converter.service;
import com.example.currency_converter.CurrencyConverterApplication;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String, Double> fetchRates() {
        String apiKey = CurrencyConverterApplication.getApiKey();
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
