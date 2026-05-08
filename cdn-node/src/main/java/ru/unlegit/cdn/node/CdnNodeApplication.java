package ru.unlegit.cdn.node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CdnNodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdnNodeApplication.class, args);
    }
}