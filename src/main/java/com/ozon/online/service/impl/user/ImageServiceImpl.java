package com.ozon.online.service.impl.user;

import com.ozon.online.dto.user.LoadImageDto;

import com.ozon.online.entity.*;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.repository.NewsImageRepository;
import com.ozon.online.repository.ProductImageRepository;
import com.ozon.online.repository.UserAvatarRepository;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.UserService;
import com.ozon.online.util.ImageUtils;
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
import java.util.concurrent.ForkJoinPool;

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
    public UserAvatar uploadImage(MultipartFile file) throws IOException {

        return userAvatarRepository.save(UserAvatar.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
//                    .type(MimeTypeUtils.IMAGE_PNG_VALUE)
                .imageData(ImageUtils.compressImage(file.getBytes())).build());

    }

    @Transactional
    public ProductImage uploadImageForProduct(MultipartFile file, User user, Product product) throws IOException, ExecutionException, InterruptedException {
        byte[] compressedImageData = compressImageInSeparateThread(file.getBytes());
        return productImageRepository.save(ProductImage.builder()
                .name(file.getOriginalFilename())
                .productId(product)
                .userId(user)
                .type(file.getContentType())
                .imageData(compressedImageData)
                .build());
    }

    @Transactional
    public News_Image uploadImageForNews(MultipartFile file, News news) throws IOException, ExecutionException, InterruptedException {
        byte[] compressedImageData = compressImageInSeparateThread(file.getBytes());
        return newsImageRepository.save(News_Image.builder()
                .name(file.getOriginalFilename())
                .news(news)
                .type(file.getContentType())
                .imageData(compressedImageData)
                .build());
    }

    public byte[] compressImageInSeparateThread(byte[] imageData) throws ExecutionException, InterruptedException {
        var future = CompletableFuture.supplyAsync(() -> ImageUtils.compressImage(imageData), new ForkJoinPool(100));
        return future.get();
    }

    @Override
    @Transactional
    public ResponseEntity<?> downloadAvatar() {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow();

        if (user.getAvatarId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no avatar"));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(ImageUtils.decompressImage(
                        user.getAvatarId().getImageData()));
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
