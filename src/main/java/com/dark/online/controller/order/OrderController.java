package com.dark.online.controller.order;

import com.dark.online.dto.product.CreateOrderForSellDto;
import com.dark.online.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderForSellDto createOrderForSellDto) {
        return orderService.addItem(createOrderForSellDto);
    }
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        return orderService.getAllOrders();
    }
}
