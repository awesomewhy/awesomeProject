package com.dark.online.service;

import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.product.CreateOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductsService {
    ResponseEntity<?> addItem(@RequestBody CreateOrderDto createOrderDto);
}
