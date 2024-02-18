package com.ozon.online.service;

import com.ozon.online.dto.chat.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ChatService {

    ResponseEntity<?> getMyChats();

//    ResponseEntity<?> openChat(@RequestParam Long chatId);
    ResponseEntity<?> openChat(@RequestParam String userNickname);

    ResponseEntity<?> sendMessage(@RequestParam String userId, @RequestBody MessageDto messageDto);

    ResponseEntity<?> deleteChatById(@RequestParam Long chatId);
}
