package com.example.dev.version1.Image;


import com.example.dev.version1.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private long id;


    @Column(name = "size")
    private long size;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "photo_url", length = 512)
    private String photoUrl;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "is_processed")
    private boolean isProcessed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;


}
