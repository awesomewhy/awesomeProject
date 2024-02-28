package com.ozon.online.controller.admin;

import com.ozon.online.dto.news.CreateNewsDto;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/news/create")
    public ResponseEntity<?> deleteChat(@RequestPart("image") MultipartFile file, @RequestPart("news") CreateNewsDto news) throws IOException, ExecutionException, InterruptedException, UserNotAuthException {
        return newsService.createNews(file, news);
    }

    @GetMapping("/news")
    public ResponseEntity<?> deleteChat() {
        return newsService.getAllService();
    }
}