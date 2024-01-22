package com.dark.online.service;

import com.dark.online.dto.order.CreateOrderForChatShowDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    ResponseEntity<?> createOrderForChat(@RequestBody CreateOrderForChatShowDto createOrderForChatShowDto);
}
