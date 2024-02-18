package com.ozon.online.dto.product;

import com.ozon.online.enums.OrderTypeEnum;
import lombok.Data;

@Data
public class CategoryDto {
    private OrderTypeEnum orderTypeEnum;
}
