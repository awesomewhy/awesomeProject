package com.ozon.online.dto.product;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MyProductDto {
    private String name;
    private LocalDateTime createdAt;
    private byte[] image;
    private Long id;
}
