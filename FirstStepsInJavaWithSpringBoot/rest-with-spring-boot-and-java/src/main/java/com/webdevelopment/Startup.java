package com.webdevelopment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
				
//		BCRYPT
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
//		String result1 = bCryptPasswordEncoder.encode("admin123");
//		String result2 = bCryptPasswordEncoder.encode("password");
//		System.out.println("My hash1 " + result1);
//		System.out.println("My hash2 " + result2);
        
//        Pbkdf2PasswordEncoder pbkdf2Encoder =
//        		new Pbkdf2PasswordEncoder(
//    				"", 8, 185000,
//    				SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
//        
//        encoders.put("pbkdf2", pbkdf2Encoder);
//        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
//        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
//        
//        String result1 = passwordEncoder.encode("admin123");
//        String result2 = passwordEncoder.encode("admin234");
//        
//        System.out.println("My hash result1 " + result1);
//        System.out.println("My hash result2 " + result2);

	}
}
