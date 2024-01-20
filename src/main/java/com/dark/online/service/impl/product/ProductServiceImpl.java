package com.dark.online.service.impl.product;

import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.product.CreateOrderDto;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.ProductRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ProductsService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductsService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    @Override
    public ResponseEntity<?> addItem(@RequestBody CreateOrderDto createOrderDto) {
        Optional<User> userOptional =  userService.getAuthenticationPrincipalUserByNickname();

        if(userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in spring context not found / user not auth"));
        }

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "product added"));
    }
}
