package ru.unlegit.cdn.coordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class CdnCoordinatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdnCoordinatorApplication.class, args);
	}
}