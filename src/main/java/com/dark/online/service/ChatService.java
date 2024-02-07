package com.dark.online.service;

import com.dark.online.dto.chat.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

public interface ChatService {

    ResponseEntity<?> getMyChats();

    ResponseEntity<?> openChat(@RequestParam Long chatId);

    ResponseEntity<?> sendMessage(@RequestPart String userId, @RequestPart MessageDto messageDto);
}
