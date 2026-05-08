package ru.unlegit.cdn.node.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.unlegit.cdn.node.model.CdnNodeAddress;
import ru.unlegit.cdn.node.model.ContentUploadResponse;
import ru.unlegit.cdn.node.model.ObjectInfo;
import ru.unlegit.cdn.node.service.CdnCoordinatorApiService;
import ru.unlegit.cdn.node.service.CdnNodeApiService;
import ru.unlegit.cdn.node.service.CdnNodeService;

import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/content")
public final class ContentController {

    private final CdnNodeService nodeService;

    @PostMapping("/upload")
    public ContentUploadResponse upload(@RequestParam("file") MultipartFile file) {
        return nodeService.uploadContent(file);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<@NonNull StreamingResponseBody> getContent(@PathVariable String contentId) {
        return nodeService.getContentStat(contentId)
                .map(objectInfo -> serveContentFromMinIO(contentId, objectInfo))
                .orElseGet(() -> serveContentFromOtherNode(contentId));
    }

    private ResponseEntity<@NonNull StreamingResponseBody> serveContentFromMinIO(
            String contentId, ObjectInfo objectInfo
    ) {
        return ResponseEntity.ok()
                .contentType(objectInfo.contentType())
                .contentLength(objectInfo.size())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + contentId + "\"")
                .body(outputStream -> {
                    try (InputStream inputStream = nodeService.getContentStream(contentId)) {
                        inputStream.transferTo(outputStream);
                    }
                });
    }

    private ResponseEntity<@NonNull StreamingResponseBody> serveContentFromOtherNode(String contentId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + contentId + "\"")
                .body(stream -> nodeService.pullContent(contentId, stream));
    }
}