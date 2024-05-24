package com.example.dev.version1.storage;


import com.example.dev.version1.exceptionHandling.NotFoundException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final MinioClient minioClient;


    @Value("${minio.bucket.name}")
    private String bucketName;


    @SneakyThrows
    public String getObjectUrl(String objectName){
        log.info("StorageService | getObjectUrl is called");
        String url = "";

        boolean flag = bucketExist();

        log.info("StorageService | getObjectUrl | flag : " + flag);
        if(bucketExist()){
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(1, TimeUnit.MINUTES)
                            .build()
            );
            log.info("StorageService | getObjectUrl | url: " + url);
        }
        return url;

    }

    @SneakyThrows
    public void putObject(String objectName, InputStream inputStream, String contentType) {
            boolean found = bucketExist();
            if (!found) makeBucket();

            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );
    }


    @SneakyThrows
    public boolean removeObject(String objectName){
        boolean flag = bucketExist();
        if(flag){
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return true;
        }
        return false;

    }




    @SneakyThrows
    public InputStream getObject(String objectName){
        return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
    }


    @SneakyThrows
    private void makeBucket(){
        minioClient
                .makeBucket(MakeBucketArgs
                        .builder()
                        .bucket(bucketName)
                        .build());
    }


    @SneakyThrows
    private boolean bucketExist(){
        boolean isExisted = minioClient.bucketExists(BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build());
        if(!isExisted){
                log.error("StorageService | BucketExists | Bucket {} is not exist",bucketName);
                throw new NotFoundException("Bucket " + bucketName + " is not exist");
            }

        return isExisted;
    }

}
