package com.example.paypalintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.paypalintegration.config")
public class PaypalIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalIntegrationApplication.class, args);
	}

}
