package ru.unlegit.cdn.coordinator.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.unlegit.cdn.coordinator.exception.IpGeoDetectionException;
import ru.unlegit.cdn.coordinator.model.GeoPosition;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Service
public final class GeoIpService {

    private static boolean isPrivateOrLocalIp(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);

            return address.isLoopbackAddress() || address.isSiteLocalAddress() || address.isLinkLocalAddress() ||
                    ipAddress.startsWith("127.") || ipAddress.startsWith("192.168.") ||
                    ipAddress.startsWith("10.") || ipAddress.startsWith("172.");
        } catch (Exception exception) {
            return true;
        }
    }

    private final DatabaseReader dbReader;

    public GeoIpService(@Value("${geoip2.database-path}") Resource resource) {
        try (InputStream stream = resource.getInputStream()) {
            dbReader = new DatabaseReader.Builder(stream).build();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public GeoPosition getGeoPosition(String ipAddress) {
        if (isPrivateOrLocalIp(ipAddress)) {
            throw new IpGeoDetectionException("ip-address '%s' is private or local".formatted(ipAddress));
        }

        try {
            CityResponse response = dbReader.city(InetAddress.getByName(ipAddress));

            double lat = response.location().latitude();
            double lon = response.location().longitude();

            return new GeoPosition(lat, lon);
        } catch (Exception exception) {
            throw new IpGeoDetectionException("Unable to detect geoposition for '%s'".formatted(ipAddress), exception);
        }
    }
}