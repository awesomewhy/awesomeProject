package com.dark.online.controller.chat;
import com.dark.online.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/chats")
    public ResponseEntity<?> getMyChats() {
        return chatService.getAllChats();
    }

    @GetMapping("/chats")
    public ResponseEntity<?> getChatWithUser(@RequestParam("id") String userId) {
        return chatService.openChat(userId);
    }
}
