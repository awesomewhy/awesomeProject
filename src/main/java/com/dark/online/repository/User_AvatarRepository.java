package com.dark.online.repository;

import com.dark.online.entity.User_Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface User_AvatarRepository extends JpaRepository<User_Avatar, Long> {
    Optional<User_Avatar> findByName(String filename);
}
