package com.example.currency_converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CurrencyConverterApplication {

	private static String apiKey;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Введите API-ключ Open Exchange Rates:");
		apiKey = scanner.nextLine();

		SpringApplication.run(CurrencyConverterApplication.class, args);
	}

	public static String getApiKey() {
		return apiKey;
	}
}
