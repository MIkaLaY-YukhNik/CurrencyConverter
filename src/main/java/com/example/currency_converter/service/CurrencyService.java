package com.example.currency_converter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private static final String API_URL = "https://openexchangerates.org/api/latest.json?app_id=dd23561225204beca0c5fe1cf9bd0d16";

    /**
     * Получает актуальные курсы валют с Open Exchange Rates
     * @return карта с курсами валют относительно USD
     */
    public Map<String, Double> fetchRates() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Запрашиваем данные у API Open Exchange Rates
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);

            // Извлекаем секцию "rates" из ответа
            Map<String, Object> rawRates = (Map<String, Object>) response.get("rates");

            // Преобразуем данные в Map<String, Double>
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

    /**
     * Конвертирует сумму из долларов (USD) в другие валюты
     * @param amount - количество долларов
     * @return карта с результатами конверсии
     */
    public Map<String, Double> convertFromUSD(double amount) {
        // Получаем актуальные курсы валют
        Map<String, Double> rates = fetchRates();

        // Конвертируем сумму из USD в другие валюты
        Map<String, Double> results = new HashMap<>();
        rates.forEach((key, value) -> results.put(key, amount * value));

        return results;
    }
}
