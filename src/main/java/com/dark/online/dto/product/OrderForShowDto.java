package com.dark.online.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderForShowDto {
    private Long id;
    private String name;
    private BigDecimal rating;
}
