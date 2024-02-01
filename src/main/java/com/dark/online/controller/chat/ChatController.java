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
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public ResponseEntity<?> sendMessage() {z
//        return chatService.getSome();
//    }
//    @MessageMapping("/message")
//    @SendTo("/topic/chat")
//    public ResponseEntity<?> sendMessage(@RequestParam("id") String userId, @RequestBody MessageDto messageDto) {
//        return chatService.sendMessage(userId, messageDto);
//    }

    @GetMapping("/chats")
    public ResponseEntity<?> getChat(@RequestParam("id") String userId) {
        return chatService.openChat(userId);
    }

    @PostMapping("/chats/send")
    public ResponseEntity<?> getChat(@RequestParam("id") String userId, @RequestBody MessageDto messageDto) {
        return chatService.sendMessage(userId, messageDto);
    }

    @PostMapping("/chats/my")
    public ResponseEntity<?> getChat() {
        return chatService.getMyChats();
    }

//    @GetMapping("/chats")
//    public ResponseEntity<?> getMyChats() {
//        return chatService.getAllChats();
//    }

}
