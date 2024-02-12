package com.dark.online.mapper;

import com.dark.online.dto.news.CreateNewsDto;
import com.dark.online.dto.news.ServiceForShowDto;
import com.dark.online.entity.News;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public News mapCreateNewsToNewsEntity(CreateNewsDto createNewsDto) {
        return News.builder()
                .description(createNewsDto.getDescription())
                .type(createNewsDto.getType())
                .links(createNewsDto.getLinks())
                .status(createNewsDto.getStatus())
                .build();
    }

    public ServiceForShowDto mapToServiceForShow(News news) {
        return ServiceForShowDto.builder()
//              .image(news.getImage().getImageData())
                .description(news.getDescription())
                .type(news.getType())
                .status(news.getStatus())
                .links(news.getLinks())
                .build();
    }
}
