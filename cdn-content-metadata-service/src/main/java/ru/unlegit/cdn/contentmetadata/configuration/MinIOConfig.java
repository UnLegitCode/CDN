package ru.unlegit.cdn.contentmetadata.configuration;

import io.minio.MinioClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinIOConfig {

    @Value("${minio.url}")
    String url;
    @Value("${minio.access-key}")
    String accessKey;
    @Value("${minio.secret-key}")
    String secretKey;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        client.setTimeout(3_000L, 60_000L, 0L);

        return client;
    }
}