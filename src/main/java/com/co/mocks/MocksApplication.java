package com.co.mocks;

import com.co.mocks.config.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties({ServiceConfig.class})
public class MocksApplication {

	public static void main(String[] args) {
		configureSystem();
		SpringApplication.run(MocksApplication.class, args);
	}

	private static void configureSystem() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
