package com.ozon.online.dto.order;

import com.ozon.online.enums.OrderTypeEnum;
import lombok.Data;

@Data
public class CreateOrderDto {
    private Long price;
    private String description;
    private OrderTypeEnum productTypeEnum;
}
