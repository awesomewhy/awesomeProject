package com.ozon.online.repository;

import com.ozon.online.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

//    @Query("SELECT c FROM Chat c WHERE :user1 MEMBER OF c.participants AND :user2 MEMBER OF c.participants")
//    Optional<Chat> findChatByUserIds(@Param("user1") User user1, @Param("user2") User user2);


//    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.id IN :userIds GROUP BY c HAVING COUNT(DISTINCT p) = :userCount")
//    List<Chat> findChatByUserIds(@Param("userIds") List<UUID> userIds, @Param("userCount") Long userCount);

    @Query("SELECT c " +
            "FROM Chat c " +
            "JOIN c.participants p1 " +
            "JOIN c.participants p2 " +
            "WHERE (p1.id = :userId1 AND p2.id = :userId2) OR (p1.id = :userId2 AND p2.id = :userId1)")
    Optional<Chat> findChatByUserIds(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

    Optional<Chat> findByIdOrderByMessagesTimeAsc(Long chatId);

//    @Query("SELECT c " +
//            "FROM Chat c " +
//            "WHERE (c.senderId.id = :userId1 AND c.companionId.id = :userId2) OR (c.senderId.id = :userId2 AND c.companionId.id = :userId1)")
//    Optional<Chat> findChatByUserIds(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}
