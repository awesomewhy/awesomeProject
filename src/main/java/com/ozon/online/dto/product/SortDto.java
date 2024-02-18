package com.ozon.online.dto.product;

import com.ozon.online.enums.PaymentTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SortDto {
    private List<CategoryDto> categories = new ArrayList<>();
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private PaymentTypeEnum paymentTypeEnum;
}
