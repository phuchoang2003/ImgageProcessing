package com.example.dev.version1.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("ImageController | uploadFile is called");
        ImageResponse response = imageService.upLoadImage(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUrl")
    public ResponseEntity<ImageResponse> getUrlFile(@RequestParam("id") long id) {
        log.info("ImageController | getUrlFile | id: {}", id);
        ImageResponse response = imageService.getUrlImage(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/deleteImage/{idImage}")
    public ResponseEntity<ImageResponse> deleteFile(@PathVariable(value = "idImage", required = true) long idImage) {
        log.info("ImageController | getUrlFile | id: {}", idImage);
        ImageResponse response = imageService.deleteSoftImage(idImage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/restore/{idImage}")
    public ResponseEntity<ImageResponse> restoreFile(@PathVariable(value = "idImage", required = true) long idImage) {
        log.info("ImageController | restoreFile | id: {}", idImage);
        ImageResponse response = imageService.restoreImageDeleted(idImage);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/download/{url}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(required = true, value = "url") String url) {
            log.info("ImageController | downloadFile | url: {}", url);
            BufferedInputStream response = (BufferedInputStream) imageService.downloadImage(url);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + url);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(response));
    }

}
