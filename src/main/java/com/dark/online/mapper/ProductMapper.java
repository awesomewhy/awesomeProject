package com.dark.online.mapper;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.ProductForShowDto;
import com.dark.online.entity.Image;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.ImageRepository;
import com.dark.online.repository.ProductRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public Product mapCreateOrderForSellDtoToProductEntity(CreateProductForSellDto createOrderForSellDto, User user) {
//        MultipartFile multipartFile
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
//        addPhotoToProduct(multipartFile, product);
        return product;
    }

//    public void addPhotoToProduct(MultipartFile multipartFile, Product product) {
//        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
//
//        if (userOptional.isEmpty()) {
//            return;
//        }
//        User user = userOptional.get();
//        Image image = imageService.uploadImage(multipartFile);
//        image.setUserId(user);
//        image.setProductId(product);
//        user.setProducts(product.getSellerId().getProducts());
//
//        imageRepository.save(image);
//        userRepository.save(user);
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
