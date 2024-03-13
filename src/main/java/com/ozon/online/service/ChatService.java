package com.ozon.online.service;

import com.ozon.online.dto.chat.MessageDto;
import com.ozon.online.exception.UserNotAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ChatService {

    ResponseEntity<?> getMyChats() throws UserNotAuthException;

//    ResponseEntity<?> openChat(@RequestParam Long chatId);
    ResponseEntity<?> openChat(@RequestParam String userNickname) throws UserNotAuthException;

    ResponseEntity<?> sendMessage(@RequestParam String userId, @RequestBody MessageDto messageDto) throws UserNotAuthException;

    ResponseEntity<?> deleteChatById(@RequestParam Long chatId) throws UserNotAuthException;
}
