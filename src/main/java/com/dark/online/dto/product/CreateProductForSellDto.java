package com.dark.online.dto.product;

import com.dark.online.enums.OrderTypeEnum;
import com.dark.online.enums.PaymentTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductForSellDto {
    private String name;
    private String image;
    private BigDecimal price;
    private String description;
    private OrderTypeEnum orderTypeEnum;
    private PaymentTypeEnum paymentTypeEnum;
}
