package com.ozon.online.controller.product;

import com.ozon.online.dto.product.CreateProductForSellDto;
import com.ozon.online.dto.product.SortDto;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestPart("image") MultipartFile multipartFile,
                                        @RequestPart("order") CreateProductForSellDto createOrderForSellDto)
            throws IOException, ExecutionException, InterruptedException, UserNotAuthException {
        return productService.addProduct(multipartFile, createOrderForSellDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false, name = "text") String text) {
        return productService.searchProduct(text);
    }

    @GetMapping("/sort")
    public ResponseEntity<?> sortProducts(@RequestBody SortDto sortDto) {
        return productService.sort(sortDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAll(@RequestParam("p") Integer offset) {
        return productService.getAllProducts(PageRequest.of(offset - 1, 9));
    }

    @GetMapping("/myproducts")
    public ResponseEntity<?> getMyProducts() {
        return productService.getMyProducts();
    }

    @GetMapping("/correctproduct")
    ResponseEntity<?> getCorrectProduct(@RequestParam("id") Long id) {
        return productService.getCorrectProduct(id);
    }
//    INSERT INTO service (name, price, description, orderTypeEnum, paymentTypeEnum, user_id)
//    VALUES ('услуга 1', 100, 'description service', 'SOME', 'VISA', 'f725741d-18a4-4818-b09a-ad792c214c66');

}
