package ru.unlegit.cdn.node.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class FileUtil {

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }

        int lastDotIndex = originalFilename.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
            return "";
        }

        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
}