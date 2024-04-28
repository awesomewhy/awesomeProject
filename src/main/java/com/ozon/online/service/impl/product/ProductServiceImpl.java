package com.ozon.online.service.impl.product;

import com.ozon.online.dto.product.*;
import com.ozon.online.entity.Product;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.mapper.ProductMapper;
import com.ozon.online.repository.ProductRepository;
import com.ozon.online.service.ProductService;
import com.ozon.online.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ResponseEntity<?> addProduct(@RequestParam(name = "image") MultipartFile multipartFile,
                                        @RequestBody CreateProductForSellDto createOrderForSellDto) throws IOException, ExecutionException, InterruptedException, UserNotAuthException {

        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );

        Product product = productMapper.mapCreateOrderForSellDtoToProductEntity(multipartFile, createOrderForSellDto, user);
        productRepository.save(product);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "product added"));
    }

    public ResponseEntity<?> sort(@RequestBody SortDto sortDto) {
        List<Product> products = new ArrayList<>(productRepository.findAll());

        ForkJoinPool forkJoinPool = new ForkJoinPool(100);

        var qwe = CompletableFuture.runAsync(() -> {
            List<Integer> categoryIndexes = sortDto.getCategories().stream()
                    .map(category -> category.getOrderTypeEnum().ordinal())
                    .toList();
            products.removeIf(product -> !categoryIndexes.contains(product.getOrderType().ordinal()));
        }, forkJoinPool);

        var qwe2 = CompletableFuture.runAsync(() -> {
            products.removeIf(product -> product.getPaymentType() != sortDto.getPaymentTypeEnum());
        }, forkJoinPool);

        var qwe3 = CompletableFuture.runAsync(() -> {
            products.removeIf(product -> product.getPrice().compareTo(sortDto.getStartPrice()) < 0 ||
                    product.getPrice().compareTo(sortDto.getEndPrice()) > 0);
        }, forkJoinPool);

        try {
            qwe.get();
            qwe2.get();
            qwe3.get();
        } catch (ExecutionException | InterruptedException e) {
            log.info("xd unlucky");
        }

//        if (sortDto.getCategories() != null) {
//            futures.add(CompletableFuture.runAsync(() -> {
//                List<Integer> categoryIndexes = sortDto.getCategories().stream()
//                        .map(category -> category.getOrderTypeEnum().ordinal())
//                        .toList();
//                products.removeIf(product -> !categoryIndexes.contains(product.getOrderType().ordinal()));
//            }, forkJoinPool));
//        }
//
//        if (sortDto.getPaymentTypeEnum() != null) {
//            futures.add(CompletableFuture.runAsync(() ->
//                            products.removeIf(product -> product.getPaymentType() != sortDto.getPaymentTypeEnum()),
//                    forkJoinPool));
//        }
//
//        if (sortDto.getStartPrice() != null && sortDto.getEndPrice() != null) {
//            futures.add(CompletableFuture.runAsync(() ->
//                            products.removeIf(product -> product.getPrice().compareTo(sortDto.getStartPrice()) < 0 ||
//                                    product.getPrice().compareTo(sortDto.getEndPrice()) > 0),
//                    forkJoinPool));
//        }
//
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//        allOf.join();

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
        Product productOptional = productRepository.findById(id).orElseThrow();
        return ResponseEntity.ok().body(productMapper.mapProductCorrectProductDto(productOptional));
    }

    @Override
    public ResponseEntity<?> getMyProducts() {
        User userOptional = userService.getAuthenticationPrincipalUserByNickname().orElseThrow();

        return ResponseEntity.ok().body(userOptional.getProducts().stream().map(
                productMapper::mapProductToMyProductDto
        ));
    }

}
