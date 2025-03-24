package com.example.currency_converter.controller;

import com.example.currency_converter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/convert")
    public Map<String, Object> convert(@RequestParam double amount, @RequestParam String targetCurrency) {
        try {
            if (!currencyService.isCurrencyValid(targetCurrency)) {
                return Map.of("error", "Неверный код валюты: " + targetCurrency);
            }

            double convertedAmount = currencyService.convertFromUSD(amount, targetCurrency);
            return Map.of(
                    "amount_in_usd", amount,
                    "target_currency", targetCurrency,
                    "converted_amount", convertedAmount
            );
        } catch (Exception e) {
            return Map.of("error", "Ошибка обработки запроса: " + e.getMessage());
        }
    }
}
