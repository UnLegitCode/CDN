package ru.unlegit.cdn.node.service;

import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MinIOService {

    final MinioClient client;
    @Value("${minio.bucket}")
    String bucket;

    @SneakyThrows
    @PostConstruct
    public void ensureBucket() {
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());

        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public void healthcheck() throws MinioException {
        client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    public List<String> listContents() {
        return StreamSupport.stream(
                client.listObjects(ListObjectsArgs.builder().bucket(bucket).build()).spliterator(), false
        ).map(item -> {
            try {
                return item.get().objectName();
            } catch (MinioException exception) {
                throw new RuntimeException(exception);
            }
        }).collect(Collectors.toCollection(LinkedList::new));
    }

    public Optional<StatObjectResponse> getObjectStat(String objectId) {
        try {
            return Optional.of(client.statObject(StatObjectArgs.builder().bucket(bucket).object(objectId).build()));
        } catch (MinioException exception) {
            return Optional.empty();
        }
    }

    @SneakyThrows
    public InputStream getObjectStream(String objectId) {
        return client.getObject(GetObjectArgs.builder().bucket(bucket).object(objectId).build());
    }

    @SneakyThrows
    public void saveObject(String objectId, MultipartFile file) {
        client.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectId)
                .stream(file.getInputStream(), file.getSize(), -1L)
                .contentType(file.getContentType())
                .build()
        );
    }

    @SneakyThrows
    public void saveObject(String objectId, InputStream stream, long contentSize, String contentType) {
        client.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectId)
                .stream(stream, -1L, -1L)
                .contentType(contentType)
                .build()
        );
    }
}