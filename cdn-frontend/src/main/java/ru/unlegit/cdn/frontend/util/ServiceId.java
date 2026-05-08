package ru.unlegit.cdn.frontend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceId {

    NODE("CDN-NODE"),
    CONTENT_METADATA_SERVICE("CDN-CONTENT-METADATA-SERVICE");

    private final String serviceId;
}