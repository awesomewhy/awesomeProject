package com.dark.online.dto.order;

import com.dark.online.enums.OrderTypeEnum;
import lombok.Data;

@Data
public class CreateOrderDto {
    private Long price;
    private String description;
    private OrderTypeEnum productTypeEnum;
}
