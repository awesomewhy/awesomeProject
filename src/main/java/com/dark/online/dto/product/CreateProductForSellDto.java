package com.dark.online.dto.product;

import com.dark.online.enums.OrderTypeEnum;
import com.dark.online.enums.PaymentTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CreateProductForSellDto implements Serializable {
    private String name;
    private BigDecimal price;
    private String description;
    private OrderTypeEnum orderTypeEnum;
    private PaymentTypeEnum paymentTypeEnum;
}
