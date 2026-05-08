package ru.unlegit.cdn.coordinator.model;

import java.util.Set;

public record CdnNodeInfo(CdnNodeAddress address, GeoPosition geoPosition, Set<String> contents) {}