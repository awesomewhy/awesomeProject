package com.dark.online.controller.product;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateProductForSellDto createOrderForSellDto) {
        return productService.addProduct(createOrderForSellDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getOrders() {
        return productService.getAllProducts();
    }
}
