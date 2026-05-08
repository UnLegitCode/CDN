package ru.unlegit.cdn.frontend.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.unlegit.cdn.frontend.dto.ContentMetadataDTO;
import ru.unlegit.cdn.frontend.dto.IdentifiedContentMetadataDTO;
import ru.unlegit.cdn.frontend.util.ServiceId;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CdnContentMetadataService {

    RestTemplate restTemplate;
    DiscoveryService discoveryService;

    private String getServiceUri() {
        return discoveryService.getServiceUri(ServiceId.CONTENT_METADATA_SERVICE);
    }

    public ContentMetadataDTO getContentMetadata(String contentId) {
        String url = "%s/metadata/%s".formatted(getServiceUri(), contentId);

        return restTemplate.getForObject(url, ContentMetadataDTO.class);
    }

    public String buildContentPreviewUrl(String contentId) {
        return "%s/preview/%s".formatted(getServiceUri(), contentId);
    }

    public List<IdentifiedContentMetadataDTO> listContent() {
        String url = "%s/metadata/list".formatted(getServiceUri());

        IdentifiedContentMetadataDTO[] result = Objects.requireNonNull(
                restTemplate.getForObject(url, IdentifiedContentMetadataDTO[].class)
        );

        return Arrays.asList(result);
    }

    public void saveContentMetadata(String contentId, ContentMetadataDTO contentMetadataDTO) {
        String url = "%s/metadata/save/%s".formatted(getServiceUri(), contentId);

        restTemplate.postForObject(url, contentMetadataDTO, Void.TYPE);
    }

    @SneakyThrows
    public void savePreview(String contentId, MultipartFile previewFile) {
        MultiValueMap<@NonNull String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(previewFile.getBytes()) {
            @Override
            public String getFilename() {
                return previewFile.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        restTemplate.exchange(
                "%s/preview/upload/%s".formatted(getServiceUri(), contentId),
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Void.class
        );
    }
}