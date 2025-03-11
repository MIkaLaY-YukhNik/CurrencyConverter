package com.example.currency_converter.modul;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate {
    private String currencyCode; // Код валюты в формате ISO 4217 (например, USD, EUR, CNY)
    private double rate;         // Курс валюты относительно базовой валюты (например, USD)
}
