package com.ozon.online.service;

import com.ozon.online.dto.news.CreateNewsDto;
import com.ozon.online.enums.NewsType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {
    ResponseEntity<?> changeLinkInCorrectType(NewsType type);
    ResponseEntity<?> getAllService();
    ResponseEntity<?> createNews(@RequestPart MultipartFile multipart, @RequestPart CreateNewsDto createNewsDto);
}
