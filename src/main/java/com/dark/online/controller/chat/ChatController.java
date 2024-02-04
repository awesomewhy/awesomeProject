package com.dark.online.controller.chat;

import com.dark.online.dto.chat.MessageDto;
import com.dark.online.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
@EnableWebSocket
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/chat")
    public ResponseEntity<?> sendMessage(@RequestParam("id") String userId, @RequestBody MessageDto messageDto) {
        return chatService.sendMessage(userId, messageDto);
    }

    @GetMapping("/chats")
    public ResponseEntity<?> getChat(@RequestParam("id") Long chatId) {
        return chatService.openChat(chatId);
    }

    @PostMapping("/chats/send")
    public ResponseEntity<?> getChat(@RequestParam("id") String userId, @RequestBody MessageDto messageDto) {
        return chatService.sendMessage(userId, messageDto);
    }

    @GetMapping("/chats/my")
    public ResponseEntity<?> getChat() {
        return chatService.getMyChats();
    }
}
