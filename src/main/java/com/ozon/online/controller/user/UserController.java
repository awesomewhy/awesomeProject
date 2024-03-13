package com.ozon.online.controller.user;

import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.AccountService;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final ImageService imageService;
    private final AccountService accountService;

    @PostMapping("/set")
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 500, multiplier = 2))
    public ResponseEntity<?> setAvatar(@RequestPart("image") MultipartFile file) throws IOException, UserNotAuthException {
        return accountService.addImage(file);
    }
    @GetMapping("/avatar")
    public ResponseEntity<?> downloadImage() {
       return imageService.downloadAvatar();
    }
}
