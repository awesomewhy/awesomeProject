package com.dark.online.service;

import com.dark.online.dto.user.LoadImageDto;
import com.dark.online.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<?> loadImage(@RequestBody LoadImageDto loadImageDto);
    UserAvatar uploadImage(MultipartFile file);
    ResponseEntity<?> downloadImage(@RequestBody Long id, Product product);
    ResponseEntity<?> downloadAvatar();
    ProductImage uploadImageForProduct(MultipartFile file, User user, Product product);
    News_Image uploadImageForNews(MultipartFile file, News news);
}
