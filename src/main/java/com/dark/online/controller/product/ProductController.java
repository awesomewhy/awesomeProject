package com.dark.online.controller.product;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.SortDto;
import com.dark.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestParam("image") MultipartFile file, @RequestBody CreateProductForSellDto createOrderForSellDto) {
        return productService.addProduct(file, createOrderForSellDto);
    }

    @GetMapping("/product")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false, name = "text") String text) {
        return productService.searchProduct(text);
    }

    @GetMapping("/sort")
    public ResponseEntity<?> sortProducts(@RequestBody SortDto sortDto) {
        return productService.sortByJsonResponse(sortDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

}
