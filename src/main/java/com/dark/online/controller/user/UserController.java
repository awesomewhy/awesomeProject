package com.dark.online.controller.user;

import com.dark.online.service.ImageService;
import com.dark.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class UserController {
    private final ImageService imageService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        return productService.addImage(file);
//        Long uploadImage = imageService.uploadImage(file);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable Long id) {
       return imageService.downloadImage(id);
    }
}
