package com.dark.online.dto.product;

import com.dark.online.entity.Image;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductForShowDto {
    private Long id;
    private String name;
    private Image image;
    private BigDecimal rating;
}
