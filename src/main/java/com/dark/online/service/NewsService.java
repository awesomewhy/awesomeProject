package com.dark.online.service;

import com.dark.online.dto.news.CreateNewsDto;
import com.dark.online.enums.NewsType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {
    ResponseEntity<?> changeLinkINCorrectType(NewsType type);
    ResponseEntity<?> getAllService();
    ResponseEntity<?> createNews(@RequestPart MultipartFile multipart, @RequestPart CreateNewsDto createNewsDto);
}
