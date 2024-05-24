package com.example.dev.version1.exceptionHandling;


import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandling {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponse message = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        log.error("Something went wrong: {}",message);

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MinioException.class)
    public ResponseEntity<ErrorResponse> handleMinioException(MinioException ex, WebRequest request) {
        ErrorResponse message = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        log.error("Something went wrong with Minio: {}", message);

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse message = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timeStamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        log.error("Not Found exception: {}",message);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InputInvalidException.class)
    public ResponseEntity<ErrorResponse> handleInputInvalidException(InputInvalidException ex, WebRequest request) {
        ErrorResponse message = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        log.error("Input is invalid:  {}",message);


        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
