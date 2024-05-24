package com.example.dev.version1.Image;


import com.example.dev.version1.exceptionHandling.NotFoundException;
import com.example.dev.version1.storage.StorageService;
import com.example.dev.version1.user.User;
import com.example.dev.version1.user.UserRepository;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    private final StorageService storageService;

    private final UserRepository userRepository;
    public ImageResponse upLoadImage(MultipartFile file){
        try (BufferedInputStream inputStream = (BufferedInputStream) file.getInputStream()) {
            LocalDateTime createAt = LocalDateTime.now();

            String objectName = createAt + "_" + file.getOriginalFilename();
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            storageService.putObject(objectName, inputStream, contentType);
            log.info("ImageService | upLoadImage | Successfully uploaded file: {} with content type : {} created at : {}", objectName, contentType,createAt);


            log.info("ImageService | upLoadImage | url: {}",objectName);

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));
            log.info("ImageService | upLoadImage | user's id: {}",user.getId());

            Image image = Image.builder()
                    .createdAt(createAt)
                    .size(file.getSize())
                    .contentType(contentType)
                    .photoName(fileName)
                    .isDeleted(false)
                    .isProcessed(false)
                    .photoUrl(objectName)
                    .user(user)
                    .build();

            imageRepository.save(image);

            return ImageResponse.builder()
                    .message("File uploaded successfully")
                    .fileName(fileName)
                    .contentType(contentType)
                    .size(file.getSize())
                    .createdAt(createAt)
                    .imageUrl(objectName)
                    .build();

        } catch (Exception e) {
            log.error("Error uploading file: {}", e.toString());
            throw new RuntimeException("Error uploading file", e);
        }
    }




    public ImageResponse getUrlImage(long id){
        Image image = imageRepository.findById(id).orElseThrow(() ->new NotFoundException("Not found image with id : " + id));
        log.info("ImageService | getUrlImage | id: {}", id);

        boolean isDeleted = image.isDeleted();

        if(isDeleted) {
            log.info("ImageService | getUrlImage | Image is deleted!");
            throw new NotFoundException("Not found image with id : " + id);
        }

        String objectName = image.getPhotoUrl();
        String contentType = image.getContentType();
        long size = image.getSize();
        String fileName = image.getPhotoName();
        LocalDateTime date = image.getCreatedAt();
        String url = storageService.getObjectUrl(objectName);


        log.info("ImageService | getUrlImage | url: {}",url);
        return ImageResponse.builder()
                .contentType(contentType)
                .size(size)
                .fileName(fileName)
                .createdAt(date)
                .message("Get url successfully")
                .imageUrl(url)
                .build();
    }



    public ImageResponse deleteSoftImage(long id){
        log.info("ImageService | deleteSoftImage | id: {}",id);
        Image image = imageRepository.findById(id).orElseThrow(() ->new NotFoundException("Not found image with id : " + id));
        boolean isDeleted = image.isDeleted();
        if(isDeleted) {
            log.info("ImageService | restoreImageDeleted | Image has been deleted !");
            throw new NotFoundException("Not found image with id : " + id);
        }

        image.setDeleted(true);


        String url = image.getPhotoUrl();
        String contentType = image.getContentType();
        long size = image.getSize();
        String fileName = image.getPhotoName();
        LocalDateTime date = image.getCreatedAt();
        imageRepository.save(image);
        return ImageResponse.builder()
                .contentType(contentType)
                .size(size)
                .fileName(fileName)
                .createdAt(date)
                .message("Delete image with url :" + url +  " successfully!")
                .imageUrl(url)
                .build();
    }

    public ImageResponse restoreImageDeleted(long id){
        log.info("ImageService | restoreImageDeleted | id: {}",id);
        Image image = imageRepository.findById(id).orElseThrow(() ->new NotFoundException("Not found image with id : " + id));

        boolean isDeleted = image.isDeleted();

        if(!isDeleted) {
            log.info("ImageService | restoreImageDeleted | Image hasn't been deleted !");
            throw new NotFoundException("Not found image with id : " + id);
        }

        image.setDeleted(false);
        String url = image.getPhotoUrl();
        String contentType = image.getContentType();
        long size = image.getSize();
        String fileName = image.getPhotoName();
        LocalDateTime date = image.getCreatedAt();


        imageRepository.save(image);
        return ImageResponse.builder()
                .contentType(contentType)
                .size(size)
                .fileName(fileName)
                .createdAt(date)
                .message("Restore image with url : " + url +  " successfully!")
                .imageUrl(url)
                .build();
    }

    public void getAllImages(){
        List<Image> images = imageRepository.findAll()
                .stream()
                .filter(image -> !image.isDeleted())
                .limit(10)
                .toList();

        log.info("ImageService | getAllImages | imagages: {}",images);

    }

    public void getAllImagesDeleted(){
        List<Image> images = imageRepository.findAll()
                .stream()
                .filter(Image::isDeleted)
                .limit(10)
                .toList();

        log.info("ImageService | getAllImagesDeleted | imagages: {}",images);
    }

    public InputStream downloadImage(String objectName){
            return storageService.getObject(objectName);
    }






}
