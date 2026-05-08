package ru.unlegit.cdn.contentmetadata.service;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.unlegit.cdn.contentmetadata.model.ObjectInfo;

import java.io.InputStream;

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

    @SneakyThrows
    public ObjectInfo getObjectInfo(String objectId) {
        StatObjectResponse objectStat = client.statObject(StatObjectArgs.builder()
                .bucket(bucket)
                .object(objectId)
                .build()
        );

        return ObjectInfo.fromMinIOStat(objectStat);
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
}