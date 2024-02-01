package com.dark.online.service;

import com.dark.online.dto.chat.MessageDto;
import com.dark.online.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ChatService {

    ResponseEntity<?> getSome();

    ResponseEntity<?> getMyChats();

    ResponseEntity<?> openChat(@RequestParam String userId);

    ResponseEntity<?> sendMessage(@RequestParam String userId, @RequestBody MessageDto messageDto);

}
