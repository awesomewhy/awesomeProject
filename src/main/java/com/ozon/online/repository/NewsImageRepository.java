package com.ozon.online.repository;

import com.ozon.online.entity.News_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsImageRepository extends JpaRepository<News_Image, Long> {
}
