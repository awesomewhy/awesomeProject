package com.ozon.online.service;

import com.ozon.online.dto.order.CreateOrderForChatShowDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    ResponseEntity<?> createOrderForChat(@PathVariable String nickname, @RequestBody CreateOrderForChatShowDto createOrderForChatShowDto);
}
