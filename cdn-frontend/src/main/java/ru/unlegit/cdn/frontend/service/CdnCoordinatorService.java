package ru.unlegit.cdn.frontend.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.unlegit.cdn.frontend.dto.CdnNodeAddress;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CdnCoordinatorService {

    final RestTemplate restTemplate;
    @Value("${coordinator.url}")
    String coordinatorUrl;

    public CdnNodeAddress findNearestNode(String clientIpAddress) {
        String url = "%s/nearest?clientIpAddress=%s".formatted(coordinatorUrl, clientIpAddress);

        return Objects.requireNonNull(restTemplate.getForObject(url, CdnNodeAddress.class));
    }

    public String buildVideoUrl(CdnNodeAddress nodeAddress, String contentId) {
        return "http://%s:%d/content/%s".formatted(nodeAddress.ipAddress(), nodeAddress.port(), contentId);
    }

    public String buildVideoUrl(String clientIpAddress, String contentId) {
        return buildVideoUrl(findNearestNode(clientIpAddress), contentId);
    }
}