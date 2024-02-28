package com.ozon.online.controller.user;

import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final ImageService imageService;
    private final ProductService productService;

    @PostMapping("/set")
    public ResponseEntity<?> setAvatar(@RequestPart("image") MultipartFile file) throws IOException, UserNotAuthException {
        return productService.addImage(file);
    }
    @GetMapping("/avatar")
    public ResponseEntity<?> downloadImage() {
       return imageService.downloadAvatar();
    }
}
