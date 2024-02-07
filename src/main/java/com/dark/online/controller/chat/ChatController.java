package com.dark.online.controller.chat;

import com.dark.online.dto.chat.MessageDto;
import com.dark.online.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chats/send")
    public ResponseEntity<?> sendMessage(@RequestPart("id") String userId, @RequestPart MessageDto messageDto) {
        return chatService.sendMessage(userId, messageDto);
    }

    @GetMapping("/chats")
    public ResponseEntity<?> getChat(@RequestParam("id") Long chatId) {
        return chatService.openChat(chatId);
    }

//    @PostMapping("/chats/send")
//    public ResponseEntity<?> sendMessage(@RequestParam("id") String userId, @RequestBody MessageDto messageDto) {
//        return chatService.sendMessage(userId, messageDto);
//    }

    @GetMapping("/chats/my")
    public ResponseEntity<?> getChat() {
        return chatService.getMyChats();
    }

//    @GetMapping("/chats")
//    public ResponseEntity<?> getMyChats() {
//        return chatService.getAllChats();
//    }
//
//    @GetMapping("/chats")
//    public ResponseEntity<?> getChatWithUser(@RequestParam("id") String userId) {
//        return chatService.openChat(userId);
//    }
}
