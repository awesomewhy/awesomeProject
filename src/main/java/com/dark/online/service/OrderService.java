package com.dark.online.service;

import com.dark.online.dto.product.CreateOrderForChatShowDto;
import com.dark.online.dto.product.CreateOrderForSellDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    ResponseEntity<?> addItem(@RequestBody CreateOrderForSellDto createOrderForSellDto);
    ResponseEntity<?> createOrderForChat(@RequestBody CreateOrderForChatShowDto createOrderForChatShowDto);
    ResponseEntity<?> getAllOrders();
}
