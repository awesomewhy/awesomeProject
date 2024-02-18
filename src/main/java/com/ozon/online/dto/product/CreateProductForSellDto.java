package com.ozon.online.dto.product;

import com.ozon.online.enums.OrderTypeEnum;
import com.ozon.online.enums.PaymentTypeEnum;
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
