package ru.unlegit.cdn.frontend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.unlegit.cdn.frontend.service.VideoService;
import ru.unlegit.cdn.frontend.util.HttpUtil;

@Log4j2
@Controller
@AllArgsConstructor
@RequestMapping("/video")
public final class VideoController {

    private final VideoService videoService;

    @GetMapping("/list")
    public String videoList(Model model) { //TODO: добавить поиск по названию
        model.addAttribute("videos", videoService.listVideos());

        return "video_list";
    }

    @GetMapping("/{videoId}")
    public String video(HttpServletRequest request, @PathVariable String videoId, Model model) {
        String clientIpAddress = HttpUtil.getClientIp(request);

        model.addAttribute("metadata", videoService.getContentMetadata(videoId));
        model.addAttribute("videoUrl", videoService.getVideoUrl(clientIpAddress, videoId));

        return "video";
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadVideo(
            HttpServletRequest request,
            @RequestParam String title,
            @RequestParam MultipartFile videoFile,
            @RequestParam MultipartFile previewFile,
            RedirectAttributes redirectAttributes
    ) {
        log.info("Uploading video");
        log.info("Title: {}", title);
        log.info("Preview file size: {}", previewFile.getSize());
        log.info("Video file size: {}", videoFile.getSize());

        videoService.uploadVideo(HttpUtil.getClientIp(request), title, videoFile, previewFile);

        redirectAttributes.addFlashAttribute("success", "Видео успешно загружено!");

        return "redirect:/video/list";
    }
}