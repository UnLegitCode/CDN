package ru.unlegit.cdn.contentmetadata.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unlegit.cdn.contentmetadata.dto.ContentMetadataDTO;
import ru.unlegit.cdn.contentmetadata.dto.IdentifiedContentMetadataDTO;
import ru.unlegit.cdn.contentmetadata.model.ContentMetadata;
import ru.unlegit.cdn.contentmetadata.repository.ContentMetadataRepository;

import java.util.List;

@Service
@AllArgsConstructor
public final class ContentMetadataService {

    private final ContentMetadataRepository contentMetadataRepository;

    public void saveContentMetadata(String contentId, ContentMetadataDTO dto) {
        ContentMetadata contentMetadata = new ContentMetadata(contentId, dto.displayName());

        contentMetadataRepository.save(contentMetadata);
    }

    private ContentMetadataDTO convert(ContentMetadata contentMetadata) {
        return new ContentMetadataDTO(contentMetadata.getDisplayName());
    }

    private IdentifiedContentMetadataDTO convertIdentified(ContentMetadata contentMetadata) {
        return new IdentifiedContentMetadataDTO(contentMetadata.getContentId(), contentMetadata.getDisplayName());
    }

    public ContentMetadataDTO getContentMetadata(String contentId) {
        return contentMetadataRepository.findById(contentId)
                .map(this::convert)
                .orElseThrow();
    }

    public List<IdentifiedContentMetadataDTO> listContentMetadata() {
        return contentMetadataRepository.findAll()
                .stream()
                .map(this::convertIdentified)
                .toList();
    }
}