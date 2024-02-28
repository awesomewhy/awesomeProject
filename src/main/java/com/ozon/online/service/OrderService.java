package com.ozon.online.service;

import com.ozon.online.dto.order.CreateOrderForChatShowDto;
import com.ozon.online.exception.UserNotAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    ResponseEntity<?> createOrderForChat(@PathVariable String nickname, @RequestBody CreateOrderForChatShowDto createOrderForChatShowDto) throws UserNotAuthException;
}
