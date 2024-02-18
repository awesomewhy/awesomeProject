package com.ozon.online.dto.news;

import com.ozon.online.enums.NewsType;
import com.ozon.online.enums.ServiceStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceForShowDto {
    private byte[] image;
    private String description;
    private NewsType type;
    private ServiceStatus status;
    private List<String> links;
}
