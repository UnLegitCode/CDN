package ru.unlegit.cdn.contentmetadata.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import ru.unlegit.cdn.contentmetadata.model.ContentMetadata;

import java.util.List;

public interface ContentMetadataRepository extends CrudRepository<@NonNull ContentMetadata, @NonNull String> {

    @NonNull
    @Override
    List<ContentMetadata> findAll();
}