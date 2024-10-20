package com.example.tfoodsapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TfoodsapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TfoodsapiApplication.class, args);
		System.out.println();
	}

	@Component
	public class PortListener implements ApplicationListener<ContextRefreshedEvent> {

		@Override
		public void onApplicationEvent(ContextRefreshedEvent event) {
			if (event.getApplicationContext() instanceof ServletWebServerApplicationContext) {
				ServletWebServerApplicationContext webServerAppContext = (ServletWebServerApplicationContext) event
						.getApplicationContext();
				int port = webServerAppContext.getWebServer().getPort();
				System.out.println("Application is running on port: " + port);
			}
		}
	}

}
