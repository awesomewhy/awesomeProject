package com.dark.online.dto.product;

import com.dark.online.entity.Category;
import com.dark.online.enums.PaymentTypeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SortDto {
    List<Category> categories = new ArrayList<>();
    private PaymentTypeEnum paymentTypeEnum;
}
