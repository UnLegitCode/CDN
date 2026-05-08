package ru.unlegit.cdn.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CdnFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdnFrontendApplication.class, args);
    }
}