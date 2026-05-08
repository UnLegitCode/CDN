package ru.unlegit.cdn.node.service;

import com.netflix.appinfo.ApplicationInfoManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@AllArgsConstructor
public final class CdnNodeInfoService {

    private final ApplicationInfoManager applicationInfoManager;

    public void updateContents(Collection<String> contents) {
        Map<String, String> metadata = applicationInfoManager.getInfo().getMetadata();

        metadata.put("contents", String.join(",", contents));

        applicationInfoManager.registerAppMetadata(metadata);
    }
}