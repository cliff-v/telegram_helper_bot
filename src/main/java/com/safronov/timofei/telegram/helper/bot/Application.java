package com.safronov.timofei.telegram.helper.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Value("${server.port}")
	private String port;

	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("=======================");
			System.out.println();
			System.out.println("	BOT IS RUNNING");
			System.out.println("	http://localhost:" + port);
			System.out.println("	DB:" + getDatabaseNameFromUrl(datasourceUrl));
			System.out.println();
			System.out.println("=======================");
		};
	}

	private String getDatabaseNameFromUrl(String url) {
		if (url != null && url.contains("/")) {
			return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
		}
		return "неизвестно";
	}
}
