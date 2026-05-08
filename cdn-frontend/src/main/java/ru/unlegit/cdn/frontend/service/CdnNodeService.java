package ru.unlegit.cdn.frontend.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.unlegit.cdn.frontend.dto.CdnNodeAddress;
import ru.unlegit.cdn.frontend.dto.VideoUploadResponse;

@Service
@AllArgsConstructor
public final class CdnNodeService {

    private final RestTemplate restTemplate;

    @SneakyThrows
    @SuppressWarnings("DataFlowIssue")
    public String uploadVideo(CdnNodeAddress nodeAddress, MultipartFile videoFile) {
        MultiValueMap<@NonNull String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(videoFile.getBytes()) {
            @Override
            public String getFilename() {
                return videoFile.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<@NonNull VideoUploadResponse> response = restTemplate.exchange(
                "http://%s:%d/content/upload".formatted(nodeAddress.ipAddress(), nodeAddress.port()),
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                VideoUploadResponse.class
        );

        return response.getBody().contentId();
    }
}