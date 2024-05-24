package com.example.dev.version1.Image;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private String message;
    private String fileName;
    private String contentType;
    private long size;
    private String imageUrl;
    private LocalDateTime createdAt;
}
