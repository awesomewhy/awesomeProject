package com.dark.online.service;

import com.dark.online.dto.user.LoadImageDto;
import com.dark.online.entity.Product_Image;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.entity.User_Avatar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<?> loadImage(@RequestBody LoadImageDto loadImageDto);
    User_Avatar uploadImage(MultipartFile file);
    ResponseEntity<?> downloadImage();
    Product_Image uploadImageForProduct(MultipartFile file, User user, Product product);
}
