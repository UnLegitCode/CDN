package ru.unlegit.cdn.coordinator.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpUtil {

    public String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded == null || forwarded.isEmpty()) {
            return request.getRemoteAddr();
        }

        return forwarded.split(",")[0].trim();
    }
}