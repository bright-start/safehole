package com.safe.demo.hole.exception;


import org.apache.commons.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<String> exception(FileUploadException e){
        return ResponseEntity.status(200).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e){
        return ResponseEntity.status(500).body("An error occurred on the server. Please try again later");
    }
}
