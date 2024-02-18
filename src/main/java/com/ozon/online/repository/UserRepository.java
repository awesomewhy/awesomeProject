package com.ozon.online.repository;

import com.ozon.online.dto.user.UserLoginDto;
import com.ozon.online.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT new com.ozon.online.dto.user.UserLoginDto(u.password, u.accountVerified, u.nickname, u.roles) FROM User u WHERE u.nickname = :nickname")
    Optional<UserLoginDto> findUserDtoByNickname(@Param("nickname") String nickname);

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname")
    Optional<User> findByNickname(@Param("nickname") String nickname);


}
