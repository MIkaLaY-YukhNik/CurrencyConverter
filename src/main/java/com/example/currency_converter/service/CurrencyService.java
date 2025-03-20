package com.example.currency_converter.service;

import com.example.currency_converter.CurrencyConverterApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    /**
     * Метод для проверки, существует ли валюта.
     * @param currencyCode Код валюты в формате ISO 4217.
     * @return true, если валюта существует; иначе false.
     */
    public boolean isCurrencyValid(String currencyCode) {
        Map<String, Double> rates = fetchRates();
        return rates.containsKey(currencyCode.toUpperCase());
    }

    /**
     * Метод для конверсии валюты из USD в целевую валюту.
     * @param amount Количество USD для конвертации.
     * @param targetCurrency Целевая валюта (код ISO 4217).
     * @return Конвертированная сумма.
     */
    public double convertFromUSD(double amount, String targetCurrency) {
        Map<String, Double> rates = fetchRates();
        double rate = rates.get(targetCurrency.toUpperCase());
        return amount * rate;
    }

    /**
     * Приватный метод для получения курсов валют с Open Exchange Rates.
     * @return Карта с курсами валют относительно USD.
     */
    private Map<String, Double> fetchRates() {
        String apiKey = CurrencyConverterApplication.getApiKey();
        String apiUrl = "https://openexchangerates.org/api/latest.json?app_id=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
            Map<String, Object> rawRates = (Map<String, Object>) response.get("rates");

            // Преобразуем курсы валют в Map<String, Double>
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
            throw new RuntimeException("Не удалось получить курсы валют: " + e.getMessage());
        }
    }
}
