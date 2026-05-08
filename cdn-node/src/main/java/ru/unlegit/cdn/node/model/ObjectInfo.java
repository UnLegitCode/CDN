package ru.unlegit.cdn.node.model;

import io.minio.StatObjectResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;

public record ObjectInfo(MediaType contentType, long size) {

    public static ObjectInfo fromMinIOStat(StatObjectResponse stat) {
        MediaType contentType = Optional.ofNullable(stat.contentType())
                .map(MediaType::parseMediaType)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return new ObjectInfo(contentType, stat.size());
    }

    public static ObjectInfo fromHeaders(HttpHeaders headers) {
        MediaType contentType = Optional.ofNullable(headers.getContentType())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return new ObjectInfo(contentType, headers.getContentLength());
    }
}