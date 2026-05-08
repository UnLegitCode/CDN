package ru.unlegit.cdn.contentmetadata.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.unlegit.cdn.contentmetadata.model.ObjectInfo;
import ru.unlegit.cdn.contentmetadata.service.MinIOService;

import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/preview")
public final class PreviewController {

    private final MinIOService minioService;

    @PostMapping("/upload/{contentId}")
    public void upload(@PathVariable String contentId, @RequestParam("file") MultipartFile file) {
        minioService.saveObject(contentId, file);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<@NonNull StreamingResponseBody> getContent(@PathVariable String contentId) {
        ObjectInfo objectInfo = minioService.getObjectInfo(contentId);

        return ResponseEntity.ok()
                .contentType(objectInfo.contentType())
                .contentLength(objectInfo.size())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + contentId + "\"")
                .body(outputStream -> {
                    try (InputStream inputStream = minioService.getObjectStream(contentId)) {
                        inputStream.transferTo(outputStream);
                    }
                });
    }
}