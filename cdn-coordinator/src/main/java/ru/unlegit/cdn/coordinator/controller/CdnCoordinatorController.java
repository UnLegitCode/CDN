package ru.unlegit.cdn.coordinator.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.unlegit.cdn.coordinator.model.CdnNodeAddress;
import ru.unlegit.cdn.coordinator.service.CdnCoordinatorService;
import ru.unlegit.cdn.coordinator.util.HttpUtil;

import java.util.UUID;

@RestController
@AllArgsConstructor
public final class CdnCoordinatorController {

    private final CdnCoordinatorService service;

    @GetMapping("/nearest")
    public CdnNodeAddress nearest(
            HttpServletRequest request,
            @RequestParam(name = "userIpAddress", required = false) String userIpAddress
    ) {
        if (userIpAddress == null) {
            userIpAddress = HttpUtil.getClientIp(request);
        }

        return service.findNearestNode(userIpAddress);
    }

    @GetMapping("/nearest/{contentId}")
    public CdnNodeAddress nearestExactly(HttpServletRequest request, @PathVariable String contentId) {
        return service.findNearestNode(HttpUtil.getClientIp(request), contentId);
    }
}