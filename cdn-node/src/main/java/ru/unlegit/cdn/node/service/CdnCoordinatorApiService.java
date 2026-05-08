package ru.unlegit.cdn.node.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.unlegit.cdn.node.model.CdnNodeAddress;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CdnCoordinatorApiService {

    final RestTemplate restTemplate;
    @Value("${coordinator.url}")
    String coordinatorUrl;

    public CdnNodeAddress findNearestNode(String contentId) {
        String url = "%s/nearest/%s".formatted(coordinatorUrl, contentId);

        return restTemplate.getForObject(url, CdnNodeAddress.class);
    }
}