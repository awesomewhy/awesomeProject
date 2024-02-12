package com.dark.online.dto.news;

import com.dark.online.enums.NewsType;
import com.dark.online.enums.ServiceStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateNewsDto {
    private String description;
    private NewsType type;
    private ServiceStatus status;
    private List<String> links;
}
