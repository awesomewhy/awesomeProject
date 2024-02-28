package com.ozon.online.service;

import com.ozon.online.dto.product.CreateProductForSellDto;
import com.ozon.online.dto.product.SortDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ResponseEntity<?> addProduct(@RequestParam(name = "image") MultipartFile multipartFile,
                                 @RequestBody CreateProductForSellDto createOrderForSellDto);

    ResponseEntity<?> getAllProducts(PageRequest pageRequest);

    ResponseEntity<?> getCorrectProduct(@RequestParam("id") Long id);

    ResponseEntity<?> addImage(@RequestParam(name = "image") MultipartFile multipartFile) throws IOException;

    ResponseEntity<?> sort(@RequestBody SortDto sortDto);

    ResponseEntity<?> searchProduct(@RequestParam("text") String text);

    ResponseEntity<?> getMyProducts();
}
