package com.dark.online.service.impl.admin;

import com.dark.online.dto.news.CreateNewsDto;
import com.dark.online.entity.News;
import com.dark.online.entity.User;
import com.dark.online.enums.NewsType;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.mapper.NewsMapper;
import com.dark.online.repository.NewsRepository;
import com.dark.online.repository.News_ImageRepository;
import com.dark.online.service.ImageService;
import com.dark.online.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements com.dark.online.service.NewsService {
    private final NewsRepository newsRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final News_ImageRepository newsImageRepository;
    private final NewsMapper newsMapper;

    @Override
    public ResponseEntity<?> changeLinkINCorrectType(NewsType type) {
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
    public ResponseEntity<?> createNews(@RequestPart MultipartFile multipart, @RequestPart CreateNewsDto createNewsDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        News news = newsMapper.mapCreateNewsToNewsEntity(createNewsDto);
        newsRepository.save(news);

        news.setImage(imageService.uploadImageForNews(multipart, news));
        newsRepository.save(news);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "news added"));
    }
}
