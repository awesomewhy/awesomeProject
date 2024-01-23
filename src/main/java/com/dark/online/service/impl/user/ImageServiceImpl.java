package com.dark.online.service.impl.user;

import com.dark.online.dto.user.LoadImageDto;

import com.dark.online.entity.Image;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.ImageRepository;
import com.dark.online.service.ImageService;
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
    private final ImageRepository imageRepository;

    @Override
    public ResponseEntity<?> loadImage(LoadImageDto loadImageDto) {
        return null;
    }

    public Long uploadImage(MultipartFile file) {
        try {
            Image imageData = imageRepository.save(Image.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes())).build());
//            if (imageData != null) {
//                return "file uploaded successfully : " + file.getOriginalFilename();
//            }
            return imageData.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> downloadImage(String fileName) {
        Optional<Image> imageOptional = imageRepository.findByName(fileName);
        if (imageOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Image not found"));
        }

        byte[] images = ImageUtils.decompressImage(imageOptional.get().getImageData());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    public byte[] qwe(String fileName) {
        Optional<Image> imageOptional = imageRepository.findByName(fileName);
        return ImageUtils.decompressImage(imageOptional.get().getImageData());
    }
}
