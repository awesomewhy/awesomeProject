package com.ozon.online.dto.product;

import com.ozon.online.enums.OrderTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CorrectProductDto {
    private Long id;
    private String name;
    private String sellerName;
    private String sellerId;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private OrderTypeEnum orderTypeEnum;
    private BigDecimal rating;
    private byte[] image;
}
