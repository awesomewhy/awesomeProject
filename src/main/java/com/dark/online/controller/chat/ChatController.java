package com.dark.online.controller.chat;
import com.dark.online.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@RestController
@RequiredArgsConstructor
@EnableWebSocket
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/some")
    public ResponseEntity<?> getSomeInfo() {
        return chatService.getSome();
    }
}
