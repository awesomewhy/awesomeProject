package com.dark.online.dto.product;

import com.dark.online.enums.OrderTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Base64;

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
