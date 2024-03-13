package com.ozon.online.service;

import com.ozon.online.dto.news.CreateNewsDto;
import com.ozon.online.enums.NewsType;
import com.ozon.online.exception.UserNotAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface NewsService {
    ResponseEntity<?> changeLinkInCorrectType(NewsType type);
    ResponseEntity<?> getAllService();
    ResponseEntity<?> createNews(@RequestPart MultipartFile multipart, @RequestPart CreateNewsDto createNewsDto) throws IOException, ExecutionException, InterruptedException, UserNotAuthException;
}
