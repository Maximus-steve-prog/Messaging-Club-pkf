package com.steven.Messaging.Club;


import com.steven.Messaging.Club.Exceptions.StorageProperties;
import com.steven.Messaging.Club.Services.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MessagingClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingClubApplication.class, args);
	}


}
