package com.ozon.online.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductForShowDto {
    private Long id;
    private String name;
    private String sellerId;
    private byte[] image;
    private BigDecimal rating;
}
