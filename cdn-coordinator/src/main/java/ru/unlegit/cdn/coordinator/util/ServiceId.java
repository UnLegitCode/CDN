package ru.unlegit.cdn.coordinator.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceId {

    NODE("cdn-node"),
    CONTENT_METADATA_SERVICE("cdn-content-metadata-service"),
    FRONTEND("cdn-frontend"),
    UNKNOWN("unknown");

    private final String serviceId;

    public static ServiceId get(String serviceId) {
        for (ServiceId id : ServiceId.values()) {
            if (id.serviceId.equalsIgnoreCase(serviceId)) {
                return id;
            }
        }
        return UNKNOWN;
    }
}