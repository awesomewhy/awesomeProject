package com.dark.online.controller.product;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.SearchDto;
import com.dark.online.dto.product.SortDto;
import com.dark.online.entity.Product;
import com.dark.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;
    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestBody CreateProductForSellDto createOrderForSellDto) {
        return productService.addProduct(createOrderForSellDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestBody SearchDto searchDto) {
        return productService.searchProduct(searchDto);
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
