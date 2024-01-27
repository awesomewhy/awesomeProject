package com.dark.online.service.impl.chat;

import com.dark.online.entity.Chat;
import com.dark.online.entity.Message;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.service.ChatService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final KafkaTemplate<String, Message> messageTemplate;
    private final UserService userService;

    public void sendMessage(@RequestBody Message message) {
        messageTemplate.send("qwe", message);
    }

    public ResponseEntity<?> openChat(@RequestParam User userId) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if(userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        List<Chat> chats = userOptional.get().getChats();
        for (int i = 0; i < chats.size(); i++) {
            if(chats.get(i).getCompanionId() == userId) {
                return ResponseEntity.ok().body(chats.get(i).getMessages());
            }
        }
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "vse ok"));
    }
}
