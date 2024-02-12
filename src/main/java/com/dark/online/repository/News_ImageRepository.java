package com.dark.online.repository;

import com.dark.online.entity.News;
import com.dark.online.entity.News_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface News_ImageRepository extends JpaRepository<News_Image, Long> {
}
