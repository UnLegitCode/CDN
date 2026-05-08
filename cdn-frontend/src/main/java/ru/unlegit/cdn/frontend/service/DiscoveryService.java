package ru.unlegit.cdn.frontend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import ru.unlegit.cdn.frontend.util.ServiceId;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class DiscoveryService {

    private final DiscoveryClient discoveryClient;

    public String getServiceUri(ServiceId serviceId) {
        return getRandomInstance(serviceId.getServiceId())
                .map(ServiceInstance::getUri)
                .map(Object::toString)
                .orElseThrow(() -> new IllegalStateException(
                        "Сервис '" + serviceId + "' не найден или недоступен"));
    }

    public Optional<ServiceInstance> getRandomInstance(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

        if (instances.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(instances.get((int) (Math.random() * instances.size())));
    }
}