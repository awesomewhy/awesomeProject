package com.ozon.online.service;

import com.ozon.online.dto.user.LoadImageDto;
import com.ozon.online.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ImageService {
    ResponseEntity<?> loadImage(@RequestBody LoadImageDto loadImageDto);
    UserAvatar uploadImage(MultipartFile file);
    ResponseEntity<?> downloadImage(@RequestBody Long id, Product product);
    ResponseEntity<?> downloadAvatar();
    ProductImage uploadImageForProduct(MultipartFile file, User user, Product product) throws IOException, ExecutionException, InterruptedException;
    News_Image uploadImageForNews(MultipartFile file, News news) throws IOException, ExecutionException, InterruptedException;
}
