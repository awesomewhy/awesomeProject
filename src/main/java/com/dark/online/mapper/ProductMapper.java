package com.dark.online.mapper;

import com.dark.online.dto.order.CreateOrderForChatShowDto;
import com.dark.online.dto.product.CreateProductForSellDto;
import com.dark.online.dto.product.ProductForShowDto;
import com.dark.online.entity.Order;
import com.dark.online.entity.Product;
import com.dark.online.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ProductMapper {
    public Product mapCreateOrderForSellDtoToProductEntity(CreateProductForSellDto createOrderForSellDto, User user) {
        Product product = Product.builder()
                .sellerId(user)
                .name(createOrderForSellDto.getName())
                .image(createOrderForSellDto.getImage())
                .price(createOrderForSellDto.getPrice())
                .discount(createOrderForSellDto.getPrice().add(createOrderForSellDto.getPrice()))
                .rating(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .description(createOrderForSellDto.getDescription())
                .orderType(createOrderForSellDto.getOrderTypeEnum())
                .paymentType(createOrderForSellDto.getPaymentTypeEnum())
                .build();
        return product;
    }

    public ProductForShowDto mapOrderToProductForShowDto(Product product) {
        return ProductForShowDto.builder()
                .id(product.getId())
                .name(product.getName())
                .rating(product.getRating())
                .build();
    }
}
