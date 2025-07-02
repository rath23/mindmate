package com.maq.mindmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.maq.mindmate.configuration.JwtProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MindmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindmateApplication.class, args);
	}

}
