package com.ozon.online.mapper;

import com.ozon.online.dto.news.CreateNewsDto;
import com.ozon.online.dto.news.ServiceForShowDto;
import com.ozon.online.entity.News;
import com.ozon.online.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class NewsMapper {
    private final NewsRepository newsRepository;

    public News mapCreateNewsToNewsEntityAndSave(CreateNewsDto createNewsDto) {
        News news = News.builder()
                .description(createNewsDto.getDescription())
                .type(createNewsDto.getType())
                .links(createNewsDto.getLinks())
                .status(createNewsDto.getStatus())
                .build();
        newsRepository.save(news);
        return news;
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
