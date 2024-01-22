package com.dark.online.service.impl.product;

import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.SortDto;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.mapper.ProductMapper;
import com.dark.online.repository.OrderRepository;
import com.dark.online.repository.ProductRepository;
import com.dark.online.service.ProductService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Override
    public ResponseEntity<?> addProduct(@RequestBody CreateProductForSellDto createOrderForSellDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if(userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in spring context not found / user not auth"));
        }
        User user = userOptional.get();
        Product product = productMapper.mapCreateOrderForSellDtoToProductEntity(createOrderForSellDto, user);

        productRepository.save(product);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "order added"));
    }

    @Override
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().body(productRepository.findAll().stream()
                .map(productMapper::mapOrderToProductForShowDto));
    }

    public ResponseEntity<?> sortByJsonResponse(@RequestBody SortDto sortDto) {
        return null;
    }

}
