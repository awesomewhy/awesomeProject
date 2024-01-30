package com.dark.online.service.impl.user;

import com.dark.online.dto.user.LoadImageDto;

import com.dark.online.entity.Product_Image;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.entity.User_Avatar;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.Product_ImageRepository;
import com.dark.online.repository.User_AvatarRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.UserService;
import com.dark.online.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final User_AvatarRepository userAvatarRepository;
    private final Product_ImageRepository productImageRepository;
    private final UserService userService;

    @Override
    public ResponseEntity<?> loadImage(LoadImageDto loadImageDto) {
        return null;
    }

    public User_Avatar uploadImage(MultipartFile file) {
        try {
            return userAvatarRepository.save(User_Avatar.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes())).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Product_Image uploadImageForProduct(MultipartFile file, User user, Product product) {
        try {
            return productImageRepository.save(Product_Image.builder()
                    .name(file.getOriginalFilename())
                    .productId(product)
                    .userId(user)
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes())).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> downloadImage() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        byte[] images = ImageUtils.decompressImage(userOptional.get().getAvatarId().getImageData());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    public byte[] qwe(String fileName) {
        Optional<User_Avatar> imageOptional = userAvatarRepository.findByName(fileName);
        return ImageUtils.decompressImage(imageOptional.get().getImageData());
    }
}
