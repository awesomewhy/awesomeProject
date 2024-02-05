package com.dark.online.dto.product;

import com.dark.online.entity.Product_Image;
import com.nimbusds.jose.util.Resource;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigDecimal;

@Data
@Builder
public class ProductForShowDto {
    private Long id;
    private String name;
    private String sellerId;
    private byte[] image;
    private BigDecimal rating;
}
