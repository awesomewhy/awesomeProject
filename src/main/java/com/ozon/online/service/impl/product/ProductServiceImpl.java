package com.ozon.online.service.impl.product;

import com.ozon.online.dto.product.*;
import com.ozon.online.entity.Product;
import com.ozon.online.entity.User;
import com.ozon.online.entity.UserAvatar;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.mapper.ProductMapper;
import com.ozon.online.repository.ProductRepository;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.repository.UserAvatarRepository;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.ProductService;
import com.ozon.online.service.UserService;
import com.ozon.online.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    @Override
    @Transactional
    public ResponseEntity<?> addProduct(@RequestParam(name = "image") MultipartFile multipartFile,
                                        @RequestBody CreateProductForSellDto createOrderForSellDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname(); // requestpart обоим сделать

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        User user = userOptional.get();
        Product product = productMapper.mapCreateOrderForSellDtoToProductEntity(multipartFile, createOrderForSellDto, user);

        productRepository.save(product);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "product added"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile multipartFile) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        User user = userOptional.get();
        if (user.getAvatarId() == null) {
            UserAvatar image = imageService.uploadImage(multipartFile);
            image.setUserId(user);
            user.setAvatarId(image);
            userAvatarRepository.save(image);
            userRepository.save(user);
        } else {
            Optional<UserAvatar> imageOptional = userAvatarRepository.findById(user.getAvatarId().getId());
            if (imageOptional.isEmpty()) {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "image not found"));
            }
            try {
                UserAvatar image = imageOptional.get();
                image.setName(multipartFile.getOriginalFilename());
                image.setType(multipartFile.getContentType());
                image.setImageData(ImageUtils.compressImage(multipartFile.getBytes()));
                userAvatarRepository.save(image);
            } catch (IOException e) {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "IOException"));
            }
        }
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "avatar added"));
    }

    public ResponseEntity<?> sort(@RequestBody SortDto sortDto) {
        List<Product> products = productRepository.findAll();

        if (!sortDto.getCategories().isEmpty()) {
            List<Integer> categoryIndexes = sortDto.getCategories().stream()
                    .map(category -> category.getOrderTypeEnum().ordinal())
                    .toList();

            products = products.stream()
                    .filter(product -> categoryIndexes.contains(product.getOrderType().ordinal()))
                    .toList();
        }

        if (sortDto.getPaymentTypeEnum() != null) {
            products = products.stream()
                    .filter(product -> product.getPaymentType() == sortDto.getPaymentTypeEnum())
                    .toList();
        }

        if (sortDto.getStartPrice() != null && sortDto.getEndPrice() != null) {
            products = products.stream()
                    .filter(product -> product.getPrice().compareTo(sortDto.getStartPrice()) >= 0 &&
                            product.getPrice().compareTo(sortDto.getEndPrice()) <= 0)
                    .toList();
        }

        List<ProductForShowDto> productForShowDto = products.stream()
                .map(productMapper::mapProductToProductForShowDto)
                .toList();

        return ResponseEntity.ok(productForShowDto);
    }

    @Override
    public ResponseEntity<?> searchProduct(@RequestParam("text") String text) {
        List<Product> products = productRepository.findProductByName(text);
        List<ProductForShowDto> productForShowDto = products.stream()
                .map(productMapper::mapProductToProductForShowDto)
                .toList();
        return ResponseEntity.ok(productForShowDto);
    }
    //qqwe

    @Override
    public ResponseEntity<?> getAllProducts(PageRequest pageRequest) {
        List<Product> products = productRepository.findAll(pageRequest).getContent();
        List<ProductForShowDto> productForShowDto = products.stream()
                .map(productMapper::mapProductToProductForShowDto)
                .toList();
        return ResponseEntity.ok(productForShowDto);
    }

    @Override
    public ResponseEntity<?> getCorrectProduct(@RequestParam("id") Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "product not found"));
        }
        return ResponseEntity.ok().body(productMapper.mapProductCorrectProductDto(productOptional.get()));
    }

    //    @Override
//    public ResponseEntity<?> getCorrectProduct(@RequestParam("id") Long id) {
//        return ResponseEntity.ok().body(productRepository.findById(id).stream()
//                .map(productMapper::mapProductCorrectProductDto));
//    }
    @Override
    public ResponseEntity<?> getMyProducts() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        return ResponseEntity.ok().body(userOptional.get().getProducts().stream().map(
                product -> MyProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .createdAt(product.getCreatedAt())
                        .image(product.getPhotoId().getImageData())
                        .build()
        ));
    }
//qq

}
