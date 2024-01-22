package com.dark.online.service;

import com.dark.online.dto.user.DeleteProfileDto;
import com.dark.online.dto.user.LoadImageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ResponseEntity<?> loadImage(@RequestBody LoadImageDto loadImageDto);
    String uploadImage(MultipartFile file) throws IOException;
    byte[] downloadImage(String fileName);
    String uploadImageToFileSystem(MultipartFile file) throws IOException;
    byte[] downloadImageFromFileSystem(String fileName) throws IOException;
}
