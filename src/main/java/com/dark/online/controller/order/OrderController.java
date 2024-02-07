package com.dark.online.controller.order;

import com.dark.online.dto.order.CreateOrderForChatShowDto;
import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.entity.User;
import com.dark.online.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/create/{nickname}")
    public ResponseEntity<?> createProduct(@PathVariable String nickname, @RequestBody CreateOrderForChatShowDto createOrderForChatShowDto) {
        return orderService.createOrderForChat(nickname, createOrderForChatShowDto);
    }
}
