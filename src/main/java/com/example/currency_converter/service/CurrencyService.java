package com.example.currency_converter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private static final String API_URL = "https://openexchangerates.org/api/latest.json?app_id=dd23561225204beca0c5fe1cf9bd0d16";

    /**
     * Получает актуальные курсы валют с Open Exchange Rates.
     * @return карта с курсами валют относительно USD.
     */
    public Map<String, Double> fetchRates() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Запрашиваем данные от Open Exchange Rates
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
     * Выполняет проверку корректности кода валюты.
     * @param currencyCode Код валюты в формате ISO 4217.
     * @return true, если валюта корректна; иначе false.
     */
    public boolean isCurrencyValid(String currencyCode) {
        Map<String, Double> rates = fetchRates();
        return rates.containsKey(currencyCode.toUpperCase());
    }

    /**
     * Конвертирует сумму из USD в указанную валюту.
     * @param amount Количество USD.
     * @param targetCurrency Целевая валюта (код ISO 4217).
     * @return Конвертированное значение.
     */
    public double convertFromUSD(double amount, String targetCurrency) {
        Map<String, Double> rates = fetchRates();

        // Проверяем, поддерживается ли валюта
        if (!rates.containsKey(targetCurrency)) {
            throw new IllegalArgumentException("Неверный код валюты: " + targetCurrency);
        }

        double rate = rates.get(targetCurrency);
        return amount * rate;
    }
}
