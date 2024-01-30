package com.dark.online.repository;

import com.dark.online.entity.User_Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface User_AvatarRepository extends JpaRepository<User_Avatar, Long> {
    Optional<User_Avatar> findByName(String filename);
}
