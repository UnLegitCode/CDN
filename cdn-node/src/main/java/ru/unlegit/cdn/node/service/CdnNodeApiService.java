package ru.unlegit.cdn.node.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.unlegit.cdn.node.model.CdnNodeAddress;
import ru.unlegit.cdn.node.model.ObjectInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CdnNodeApiService {

    RestTemplate restTemplate;
    MinIOService minIOService;

    public void pullContent(CdnNodeAddress source, String contentId, OutputStream userOutputStream) {
        String url = "http://%s:%d/content/%s".formatted(source.ipAddress(), source.port(), contentId);

        restTemplate.execute(
                url, HttpMethod.GET,
                request -> {},
                response -> {
                    ObjectInfo objectInfo = ObjectInfo.fromHeaders(response.getHeaders());

                    InputStream sourceStream = response.getBody();

                    PipedOutputStream pipedOut = new PipedOutputStream();
                    PipedInputStream pipedIn = new PipedInputStream(pipedOut, 128 * 1024);

                    try (sourceStream; pipedOut; userOutputStream) {
                        byte[] buffer = new byte[64 * 1024];
                        int bytesRead;

                        while ((bytesRead = sourceStream.read(buffer)) != -1) {
                            userOutputStream.write(buffer, 0, bytesRead);
                            userOutputStream.flush();                 // важно для видео!

                            pipedOut.write(buffer, 0, bytesRead);
                            pipedOut.flush();
                        }


                        minIOService.saveObject(
                                contentId, pipedIn, objectInfo.size(), objectInfo.contentType().toString()
                        );

                        return true;
                    } catch (Exception exception) {
                        return false;
                    }
                }
        );
    }
}