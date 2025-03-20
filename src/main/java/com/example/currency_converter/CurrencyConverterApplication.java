package com.example.currency_converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CurrencyConverterApplication {

	// Статическая переменная для хранения API-ключа
	private static String apiKey;

	public static void main(String[] args) {
		// Запрашиваем API-ключ у пользователя
		Scanner scanner = new Scanner(System.in);
		System.out.println("Введите API-ключ Open Exchange Rates:");
		apiKey = scanner.nextLine();

		// Запускаем Spring Boot приложение
		SpringApplication.run(CurrencyConverterApplication.class, args);
	}

	// Метод для получения API-ключа в других частях программы
	public static String getApiKey() {
		return apiKey;
	}
}
