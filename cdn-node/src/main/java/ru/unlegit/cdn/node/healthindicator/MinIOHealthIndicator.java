package ru.unlegit.cdn.node.healthindicator;

import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.unlegit.cdn.node.service.MinIOService;

@Component
@AllArgsConstructor
public final class MinIOHealthIndicator implements HealthIndicator {

    private final MinIOService minioService;

    @Override
    public Health health() {
        try {
            minioService.healthcheck();

            return Health.up().build();
        } catch (MinioException exception) {
            return Health.down(exception).build();
        }
    }
}