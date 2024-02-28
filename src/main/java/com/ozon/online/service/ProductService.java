package com.ozon.online.service;

import com.ozon.online.dto.product.CreateProductForSellDto;
import com.ozon.online.dto.product.SortDto;
import com.ozon.online.exception.UserNotAuthException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.concurrent.ExecutionException;


public interface
ProductService {
    ResponseEntity<?> addProduct(@RequestParam(name = "image") MultipartFile multipartFile,
                                 @RequestBody CreateProductForSellDto createOrderForSellDto) throws IOException, ExecutionException, InterruptedException, UserNotAuthException;

    ResponseEntity<?> getAllProducts(PageRequest pageRequest);

    ResponseEntity<?> getCorrectProduct(@RequestParam("id") Long id);

    ResponseEntity<?> addImage(@RequestParam(name = "image") MultipartFile multipartFile) throws IOException, UserNotAuthException;

    ResponseEntity<?> sort(@RequestBody SortDto sortDto);

    ResponseEntity<?> searchProduct(@RequestParam("text") String text);

    ResponseEntity<?> getMyProducts();
}
