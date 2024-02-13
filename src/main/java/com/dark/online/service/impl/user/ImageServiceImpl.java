package com.dark.online.service.impl.user;

import com.dark.online.dto.user.LoadImageDto;

import com.dark.online.entity.*;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.NewsImageRepository;
import com.dark.online.repository.ProductImageRepository;
import com.dark.online.repository.UserAvatarRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.UserService;
import com.dark.online.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final UserAvatarRepository userAvatarRepository;
    private final ProductImageRepository productImageRepository;
    private final NewsImageRepository newsImageRepository;
    private final UserService userService;

    @Override
    public ResponseEntity<?> loadImage(LoadImageDto loadImageDto) {
        return null;
    }
    @Transactional
    public UserAvatar uploadImage(MultipartFile file) {
        try {
            return userAvatarRepository.save(UserAvatar.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
//                    .type(MimeTypeUtils.IMAGE_PNG_VALUE)
                    .imageData(ImageUtils.compressImage(file.getBytes())).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //    public Product_Image uploadImageForProduct(MultipartFile file, User user, Product product) {
//        try {
//            return productImageRepository.save(Product_Image.builder()
//                    .name(file.getOriginalFilename())
//                    .productId(product)
//                    .userId(user)
//                    .type(file.getContentType())
//                    .imageData(ImageUtils.compressImage(file.getBytes())).build());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Transactional
    public ProductImage uploadImageForProduct(MultipartFile file, User user, Product product) {
        try {
            byte[] compressedImageData = compressImageInSeparateThread(file.getBytes());
            return productImageRepository.save(ProductImage.builder()
                    .name(file.getOriginalFilename())
                    .productId(product)
                    .userId(user)
                    .type(file.getContentType())
                    .imageData(compressedImageData)
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public News_Image uploadImageForNews(MultipartFile file, News news) {
        try {
            byte[] compressedImageData = compressImageInSeparateThread(file.getBytes());
            return newsImageRepository.save(News_Image.builder()
                    .name(file.getOriginalFilename())
                    .news(news)
                    .type(file.getContentType())
                    .imageData(compressedImageData)
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public byte[] compressImageInSeparateThread(byte[] imageData) {
        CompletableFuture<byte[]> future = CompletableFuture.supplyAsync(() -> ImageUtils.compressImage(imageData));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> downloadAvatar() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        if (userOptional.get().getAvatarId() == null) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no avatar"));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(ImageUtils.decompressImage(
                        userOptional.get().getAvatarId().getImageData()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> downloadImage(@RequestBody Long id, Product product) {
        byte[] images = ImageUtils.decompressImage(product.getPhotoId().getImageData());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    public byte[] qwe(String fileName) {
        Optional<UserAvatar> imageOptional = userAvatarRepository.findByName(fileName);
        return ImageUtils.decompressImage(imageOptional.get().getImageData());
    }
}
