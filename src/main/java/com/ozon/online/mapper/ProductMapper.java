package com.ozon.online.mapper;

import com.ozon.online.dto.product.CorrectProductDto;
import com.ozon.online.dto.product.CreateProductForSellDto;
import com.ozon.online.dto.product.MyProductDto;
import com.ozon.online.dto.product.ProductForShowDto;
import com.ozon.online.entity.Product;
import com.ozon.online.entity.User;
import com.ozon.online.entity.UserAvatar;
import com.ozon.online.repository.ProductRepository;
import com.ozon.online.repository.ProductImageRepository;
import com.ozon.online.repository.UserAvatarRepository;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.UserService;
import com.ozon.online.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ImageService imageService;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final UserAvatarRepository userAvatarRepository;

    public Product mapCreateOrderForSellDtoToProductEntity(MultipartFile multipartFile,
                                                           CreateProductForSellDto createOrderForSellDto,
                                                           User user) throws IOException, ExecutionException, InterruptedException {
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
//        byte[] imageData = productImageRepository.findById(product.getPhotoId().getId()).get().getImageData();
//        byte[] base64ImageData = ImageUtils.decompressImage(productImageRepository.findById(product.getPhotoId().getId()).get().getImageData());
//        String imageSrc = "data:image/jpg;base64," + Arrays.toString(base64ImageData);
        return CorrectProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sellerId(String.valueOf(product.getSellerId().getId()))
                .sellerName(String.valueOf(product.getSellerId().getNickname()))
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .rating(product.getRating())
                .image(ImageUtils.decompressImage(productImageRepository.findById(product.getPhotoId().getId()).get().getImageData()))
                .build();
    }

    public void mapMultipartFileToUserAvatarAndSave(UserAvatar userAvatar, MultipartFile multipartFile) throws IOException {
        UserAvatar userAvatar1 = userAvatar.builder()
                .name(multipartFile.getOriginalFilename())
                .type(multipartFile.getContentType())
                .imageData(ImageUtils.compressImage(multipartFile.getBytes()))
                .build();
        userAvatarRepository.save(userAvatar1);
    }

    public void saveUserAvatar(MultipartFile multipartFile, User user) throws IOException {
        UserAvatar image = imageService.uploadImage(multipartFile);
        image.setUserId(user);
        user.setAvatarId(image);
        userAvatarRepository.save(image);
        userRepository.save(user);
    }

    public MyProductDto mapProductToMyProductDto(Product product) {
        return MyProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .createdAt(product.getCreatedAt())
                .image(product.getPhotoId().getImageData())
                .build();
    }
}
