package com.dark.online.controller.chat;
import com.dark.online.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@EnableWebSocket
public class ChatController {
    private final ChatService chatService;

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public ResponseEntity<?> sendMessage() {
//        return chatService.getSome();
//    }
    @MessageMapping("/message")
    @SendTo("/topic/chat")
    public ResponseEntity<?> sendMessage() {
        return chatService.getSome();
    }

    @GetMapping("/chats/{userId}")
    public ResponseEntity<?> getChat(@RequestParam("id") String userId) {
        return chatService.openChat(userId);
    }

    @GetMapping("/chats")
    public ResponseEntity<?> getMyChats() {
        return chatService.getAllChats();
    }

}
