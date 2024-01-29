package com.dark.online.repository;

import com.dark.online.entity.Image;
import com.dark.online.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String fileName);

    Optional<Image> findByUserId(User id);

}
