package ru.skillbox.socialnetwork.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NotificationsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationsMicroserviceApplication.class, args);
	}

}
