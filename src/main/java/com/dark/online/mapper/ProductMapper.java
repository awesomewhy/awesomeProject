package com.dark.online.mapper;

import com.dark.online.dto.product.CreateOrderDto;
import com.dark.online.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private Product mapToProductEntity(CreateOrderDto createOrderDto) {
        Product product = Product.builder()

                .build();
        return product;
    }
}
