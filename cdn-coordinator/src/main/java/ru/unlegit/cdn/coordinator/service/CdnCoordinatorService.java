package ru.unlegit.cdn.coordinator.service;

import com.netflix.appinfo.InstanceInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.unlegit.cdn.coordinator.exception.CdnNodeNotFoundException;
import ru.unlegit.cdn.coordinator.model.CdnNodeAddress;
import ru.unlegit.cdn.coordinator.model.CdnNodeInfo;
import ru.unlegit.cdn.coordinator.model.GeoPosition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CdnCoordinatorService {

    GeoIpService geoIpService;
    ConcurrentMap<String, CdnNodeInfo> nodes = new ConcurrentHashMap<>();

    private CdnNodeInfo extractNodeInfo(InstanceInfo instanceInfo) {
        Map<String, String> metadata = instanceInfo.getMetadata();
        String ipAddress = instanceInfo.getHostName();
        CdnNodeAddress address = new CdnNodeAddress(ipAddress, instanceInfo.getPort());
        GeoPosition geoPosition = geoIpService.getGeoPosition(ipAddress);
        Set<String> contents = Arrays.stream(metadata.get("contents").split(",")).collect(Collectors.toSet());

        return new CdnNodeInfo(address, geoPosition, contents);
    }

    public void updateNode(InstanceInfo instanceInfo) {
        String instanceId = instanceInfo.getInstanceId();
        CdnNodeInfo nodeInfo = extractNodeInfo(instanceInfo);

        nodes.put(instanceId, nodeInfo);
    }

    public void removeNode(String instanceId) {
        nodes.remove(instanceId);
    }

    private CdnNodeAddress findNearestNode(String clientIp, Stream<CdnNodeInfo> nodeStream) {
        GeoPosition clientGeoPosition = geoIpService.getGeoPosition(clientIp);

        return nodeStream
                .min(Comparator.comparingDouble(node -> clientGeoPosition.distanceTo(node.geoPosition())))
                .orElseThrow(CdnNodeNotFoundException::new)
                .address();
    }

    public CdnNodeAddress findNearestNode(String clientIp) {
        return findNearestNode(clientIp, nodes.values().stream());
    }

    public CdnNodeAddress findNearestNode(String clientIp, String contentId) {
        Stream<CdnNodeInfo> contentNodeStream = nodes.values()
                .stream()
                .filter(node -> node.contents().contains(contentId));

        return findNearestNode(clientIp, contentNodeStream);
    }
}