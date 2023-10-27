package com.bookstore.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class PasswordGeneratorEncoder {
	public static void main(String[] args) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// Generate a random user ID with 5 digits
		String userId = generateRandomUserId();

		// Password to encode
		String password = "admin";

		// Encode the password
		String encodedPassword = passwordEncoder.encode(password);

		System.out.println("User ID: " + userId);
		System.out.println("Encoded Password: " + encodedPassword);
	}

	private static String generateRandomUserId() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder userIdBuilder = new StringBuilder(5);

		for (int i = 0; i < 5; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			userIdBuilder.append(randomChar);
		}

		return userIdBuilder.toString();
	}
}
