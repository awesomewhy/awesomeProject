package com.dark.online.mapper;

import com.dark.online.dto.order.CreateOrderForChatShowDto;
import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.ProductForShowDto;
import com.dark.online.entity.Image;
import com.dark.online.entity.Order;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.repository.ImageRepository;
import com.dark.online.repository.ProductRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.impl.user.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Product mapCreateOrderForSellDtoToProductEntity(MultipartFile multipartFile, CreateProductForSellDto createOrderForSellDto, User user) {
        Product product = Product.builder()
                .sellerId(user)
                .name(createOrderForSellDto.getName())
                .price(createOrderForSellDto.getPrice())
                .discount(createOrderForSellDto.getPrice().add(createOrderForSellDto.getPrice()))
                .rating(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .description(createOrderForSellDto.getDescription())
                .orderType(createOrderForSellDto.getOrderTypeEnum())
                .paymentType(createOrderForSellDto.getPaymentTypeEnum())
                .build();
        Image imageId = imageService.uploadImage(multipartFile);
        product.setPhotoId(imageId);
        return product;
    }

//    public void imageTest(MultipartFile multipartFile) {
//        Long id = imageService.uploadImage(multipartFile);
//        Optional<Image> image = imageRepository.findById(id);
//        if (image.isEmpty()) {
//            return;
//        }
//    }

    public ProductForShowDto mapProductToProductForShowDto(Product product) {
        return ProductForShowDto.builder()
                .id(product.getId())
                .image(product.getPhotoId())
                .name(product.getName())
                .rating(product.getRating())
                .build();
    }
}
