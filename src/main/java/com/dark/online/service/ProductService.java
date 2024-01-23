package com.dark.online.service;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.SearchDto;
import com.dark.online.dto.product.SortDto;
import com.dark.online.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> addProduct(@RequestBody CreateProductForSellDto createOrderForSellDto);
    ResponseEntity<?> getAllProducts();
    ResponseEntity<?> addImage(@PathVariable MultipartFile multipartFile);
    ResponseEntity<?> sortByJsonResponse(@RequestBody SortDto sortDto);
    ResponseEntity<?> searchProduct(@RequestBody SearchDto searchDto);
}
