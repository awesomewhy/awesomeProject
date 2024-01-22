package com.dark.online.service;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.SortDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductService {
    ResponseEntity<?> addProduct(@RequestBody CreateProductForSellDto createOrderForSellDto);
    ResponseEntity<?> getAllProducts();
    ResponseEntity<?> sortByJsonResponse(@RequestBody SortDto sortDto);
}
