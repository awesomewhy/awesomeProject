package com.dark.online.mapper;

import com.dark.online.dto.product.CorrectProductDto;
import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.ProductForShowDto;
import com.dark.online.entity.Product;
import com.dark.online.entity.Product_Image;
import com.dark.online.entity.User;
import com.dark.online.repository.ProductRepository;
import com.dark.online.repository.Product_ImageRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.ProductService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ImageService imageService;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final Product_ImageRepository productImageRepository;

    public Product mapCreateOrderForSellDtoToProductEntity(MultipartFile multipartFile,
                                                           CreateProductForSellDto createOrderForSellDto,
                                                           User user) {
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
        product.setPhotoId(imageService.uploadImageForProduct(multipartFile, user, product));
        return product;
    }

    public void addPhotoToProduct(MultipartFile multipartFile, Product product) {



//        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
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
    }

    public ProductForShowDto mapProductToProductForShowDto(Product product) {
        return ProductForShowDto.builder()
                .id(product.getId())
                .sellerId(String.valueOf(product.getSellerId().getId()))
                .image(productImageRepository.findById(product.getPhotoId().getId()).get().getImageData())
                .name(product.getName())
                .rating(product.getRating())
                .build();
    }

    public CorrectProductDto mapProductCorrectProductDto(Product product) {
        return CorrectProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sellerId(String.valueOf(product.getSellerId().getId()))
                .sellerName(String.valueOf(product.getSellerId().getNickname()))
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .rating(product.getRating())
                .image(productImageRepository.findById(product.getPhotoId().getId()).get().getImageData())
                .build();
    }
}
