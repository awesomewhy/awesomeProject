package com.dark.online.service;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.SortDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ResponseEntity<?> addProduct(@RequestParam(name = "image") MultipartFile multipartFile,
                                 @RequestBody CreateProductForSellDto createOrderForSellDto);

    ResponseEntity<?> getAllProducts();

    ResponseEntity<?> getCorrectProduct(@RequestParam("id") Long id);

    ResponseEntity<?> addImage(@RequestParam(name = "image") MultipartFile multipartFile);

    ResponseEntity<?> sort(@RequestBody SortDto sortDto);

    ResponseEntity<?> searchProduct(@RequestParam(required = false, name = "text") String text);
}
