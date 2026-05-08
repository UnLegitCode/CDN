package ru.unlegit.cdn.contentmetadata.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.unlegit.cdn.contentmetadata.dto.ContentMetadataDTO;
import ru.unlegit.cdn.contentmetadata.dto.IdentifiedContentMetadataDTO;
import ru.unlegit.cdn.contentmetadata.service.ContentMetadataService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/metadata")
public final class ContentMetadataController {

    private final ContentMetadataService contentMetadataService;

    @PostMapping("/save/{contentId}")
    public void saveContentMetadata(@PathVariable String contentId, @RequestBody ContentMetadataDTO dto) {
        contentMetadataService.saveContentMetadata(contentId, dto);
    }

    @GetMapping("/{contentId}")
    public ContentMetadataDTO getContentMetadata(@PathVariable String contentId) {
        return contentMetadataService.getContentMetadata(contentId);
    }

    @GetMapping("/list")
    public List<IdentifiedContentMetadataDTO> listContentMetadata() {
        return contentMetadataService.listContentMetadata();
    }
}