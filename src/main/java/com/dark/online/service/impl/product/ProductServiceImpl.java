package com.dark.online.service.impl.product;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.ProductForShowDto;
import com.dark.online.dto.product.SortDto;
import com.dark.online.entity.Image;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.mapper.ProductMapper;
import com.dark.online.repository.ProductRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.ProductService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    @Override
    public ResponseEntity<?> addProduct(@RequestParam(name = "image") MultipartFile multipartFile, @RequestBody CreateProductForSellDto createOrderForSellDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in spring context not found / user not auth"));
        }
        User user = userOptional.get();
        Product product = productMapper.mapCreateOrderForSellDtoToProductEntity(multipartFile, createOrderForSellDto, user);

        productRepository.save(product);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "order added"));
    }

    @Override
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile multipartFile) {
        Image image = imageService.uploadImage(multipartFile);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "qweasd"));
    }

    public ResponseEntity<?> sortByJsonResponse(@RequestBody SortDto sortDto) {
        List<Product> products = productRepository.findAll();

        if (!sortDto.getCategories().isEmpty()) {
            List<Integer> categoryIndexes = sortDto.getCategories().stream()
                    .map(category -> category.getOrderTypeEnum().ordinal())
                    .toList();

            products = products.stream()
                    .filter(product -> categoryIndexes.contains(product.getOrderType().ordinal()))
                    .collect(Collectors.toList());
        }

        if (sortDto.getPaymentTypeEnum() != null) {
            products = products.stream()
                    .filter(product -> product.getPaymentType() == sortDto.getPaymentTypeEnum())
                    .collect(Collectors.toList());
        }

        if (sortDto.getStartPrice() != null && sortDto.getEndPrice() != null) {
            products = products.stream()
                    .filter(product -> product.getPrice().compareTo(sortDto.getStartPrice()) >= 0 &&
                            product.getPrice().compareTo(sortDto.getEndPrice()) <= 0)
                    .collect(Collectors.toList());
        }

        List<ProductForShowDto> productForShowDto = products.stream()
                .map(productMapper::mapProductToProductForShowDto)
                .toList();

        return ResponseEntity.ok(productForShowDto);
    }

    public ResponseEntity<?> searchProduct(@RequestParam String text) {
        List<Product> products = productRepository.findProductByName(text);
        List<ProductForShowDto> productForShowDto = products.stream()
                .map(productMapper::mapProductToProductForShowDto)
                .toList();
        return ResponseEntity.ok(productForShowDto);
    }

    @Override
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().body(productRepository.findAll().stream()
                .map(productMapper::mapProductToProductForShowDto));
    }

}
