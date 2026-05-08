package ru.unlegit.cdn.node.service;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.unlegit.cdn.node.model.CdnNodeAddress;
import ru.unlegit.cdn.node.model.ContentUploadResponse;
import ru.unlegit.cdn.node.model.ObjectInfo;
import ru.unlegit.cdn.node.util.FileUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CdnNodeService {

    MinIOService minIOService;
    CdnNodeInfoService nodeInfoService;
    CdnCoordinatorApiService coordinatorApiService;
    CdnNodeApiService nodeApiService;

    @PostConstruct
    public void init() {
        updateContentList();
    }

    private void updateContentList() {
        nodeInfoService.updateContents(minIOService.listContents());
    }

    public Optional<ObjectInfo> getContentStat(String contentId) {
        return minIOService.getObjectStat(contentId).map(ObjectInfo::fromMinIOStat);
    }

    public InputStream getContentStream(String contentId) {
        return minIOService.getObjectStream(contentId);
    }

    public ContentUploadResponse uploadContent(MultipartFile file) {
        String contentId = "%s.%s".formatted(UUID.randomUUID().toString(), FileUtil.getFileExtension(file));

        minIOService.saveObject(contentId, file);
        updateContentList();

        return new ContentUploadResponse(contentId);
    }

    public void pullContent(String contentId, OutputStream userOutputStream) {
        CdnNodeAddress source = coordinatorApiService.findNearestNode(contentId);

        nodeApiService.pullContent(source, contentId, userOutputStream);
    }
}