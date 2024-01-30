package com.dark.online.dto.product;

import com.dark.online.entity.Product_Image;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductForShowDto {
    private Long id;
    private String name;
    private Product_Image image;
    private BigDecimal rating;
}
