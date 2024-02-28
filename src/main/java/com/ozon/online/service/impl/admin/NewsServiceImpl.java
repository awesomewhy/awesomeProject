package com.ozon.online.service.impl.admin;

import com.ozon.online.dto.news.CreateNewsDto;
import com.ozon.online.entity.News;
import com.ozon.online.entity.User;
import com.ozon.online.enums.NewsType;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.mapper.NewsMapper;
import com.ozon.online.repository.NewsRepository;
import com.ozon.online.repository.NewsImageRepository;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.NewsService;
import com.ozon.online.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final NewsImageRepository newsImageRepository;
    private final NewsMapper newsMapper;

    @Override
    public ResponseEntity<?> changeLinkInCorrectType(NewsType type) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAllService() {
        return ResponseEntity.ok().body(newsRepository.findAll().stream().map(
                newsMapper::mapToServiceForShow
        ));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createNews(@RequestPart MultipartFile multipart, @RequestPart CreateNewsDto createNewsDto)
            throws IOException, ExecutionException, InterruptedException, UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );

        News news = newsMapper.mapCreateNewsToNewsEntity(createNewsDto);
        newsRepository.save(news);

        news.setImage(imageService.uploadImageForNews(multipart, news));
        newsRepository.save(news);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "news added"));
    }
}
