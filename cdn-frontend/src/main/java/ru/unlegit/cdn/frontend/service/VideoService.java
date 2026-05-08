package ru.unlegit.cdn.frontend.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.unlegit.cdn.frontend.dto.CdnNodeAddress;
import ru.unlegit.cdn.frontend.dto.ContentMetadataDTO;
import ru.unlegit.cdn.frontend.dto.VideoDTO;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class VideoService {

    CdnContentMetadataService contentMetadataService;
    CdnCoordinatorService coordinatorService;
    CdnNodeService nodeService;

    public ContentMetadataDTO getContentMetadata(String videoId) {
        return contentMetadataService.getContentMetadata(videoId);
    }

    public String getVideoUrl(String clientIpAddress, String videoId) {
        return coordinatorService.buildVideoUrl(clientIpAddress, videoId);
    }

    public String getPreviewUrl(String videoId) {
        return contentMetadataService.buildContentPreviewUrl(videoId);
    }

    public List<VideoDTO> listVideos() {
        return contentMetadataService.listContent().stream()
                .map(contentMetadata -> new VideoDTO(
                        contentMetadata.videoId(),
                        getPreviewUrl(contentMetadata.videoId()),
                        contentMetadata.displayName()
                ))
                .toList();
    }

    public void uploadVideo(
            String clientIpAddress, String displayName, MultipartFile videoFile, MultipartFile previewFile
    ) {
        CdnNodeAddress nodeAddress = coordinatorService.findNearestNode(clientIpAddress);
        String videoId = nodeService.uploadVideo(nodeAddress, videoFile);
        contentMetadataService.saveContentMetadata(videoId, new  ContentMetadataDTO(displayName));
        contentMetadataService.savePreview(videoId, previewFile);
    }
}