package com.dark.online.dto.product;

import com.dark.online.enums.ProductTypeEnum;
import lombok.Data;

@Data
public class CreateOrderDto {
    private ProductTypeEnum productTypeEnum;
    private Long price;
    private String description;
}
