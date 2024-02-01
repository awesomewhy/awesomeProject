package com.dark.online.service.impl.chat;

import com.dark.online.dto.chat.ChatsDto;
import com.dark.online.dto.chat.MessageDto;
import com.dark.online.dto.chat.MessageForChatDto;
import com.dark.online.entity.Chat;
import com.dark.online.entity.Message;
import com.dark.online.entity.User;
import com.dark.online.enums.MessageStatus;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.ChatRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ChatService;
import com.dark.online.service.ProductService;
import com.dark.online.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ProductService productService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseEntity<?> sendMessage(@RequestParam String userId, @RequestBody MessageDto messageDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        Optional<User> companionOptional = userRepository.findById(UUID.fromString(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        if (companionOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "companion not found"));
        }
        if(userId.equals(String.valueOf(userOptional.get().getId()))) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "you can't send a message to yourself"));
        }
        User user = User.builder()
                .id(UUID.fromString(userId))
                .build();
        Optional<Chat> chatOptional = chatRepository.findChatByUser1AndUser2(userOptional.get(), companionOptional.get());

        if (chatOptional.isEmpty()) {
            Chat chat = Chat.builder()
                    .user1(userOptional.get())
                    .user2(companionOptional.get())
                    .messages(new ArrayList<>())
                    .images(new ArrayList<>())
                    .build();
            chat.getMessages().add(
                    Message.builder()
                            .chat(chat)
                            .message(messageDto.getMessage())
                            .sender(userOptional.get())
                            .recipient(companionOptional.get())
                            .messageStatus(MessageStatus.DELIVERED)
                            .time(LocalDateTime.now())
                            .build());
            Chat savedChat = chatRepository.save(chat);
            userRepository.save(userOptional.get());
            return ResponseEntity.ok().body(chat.getMessages().stream().map(
                    message -> MessageForChatDto.builder()
                            .name(userOptional.get().getId().equals(message.getSender().getId())
                                    ? message.getSender().getNickname() : message.getRecipient().getNickname())
                            .localDateTime(message.getTime())
                            .message(message.getMessage())
                            .build()
            ));
        } else {
            List<Message> messageList = chatOptional.get().getMessages();
            messageList.add(
                    Message.builder()
                            .chat(chatOptional.get())
                            .message(messageDto.getMessage())
                            .sender(userOptional.get())
                            .recipient(companionOptional.get())
                            .messageStatus(MessageStatus.DELIVERED)
                            .time(LocalDateTime.now())
                            .build());
            return ResponseEntity.ok().body(chatOptional.get().getMessages().stream().map(
                    message -> MessageForChatDto.builder()
                            .name(userOptional.get().getId().equals(message.getSender().getId())
                                    ? message.getSender().getNickname() : message.getRecipient().getNickname())
                            .localDateTime(message.getTime())
                            .message(message.getMessage())
                            .build()
            ));
        }
    }
    @Override
    public ResponseEntity<?> getMyChats() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        List<Chat> chats = userOptional.get().getChats();
        if(chats.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no chats"));
        }
        if(chats.get(0).getMessages().isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no message"));
        }

        return ResponseEntity.ok().body(userOptional.get().getChats()
                .stream().map(
                        chat -> ChatsDto.builder()
                                .messageType(MessageStatus.DELIVERED)
                                .companionName(chat.getUser2().getNickname())
                                .message(chat.getMessages().get(chat.getMessages().size() - 1).getMessage())
                                .build()
                ));
    }
    @Override
    public ResponseEntity<?> openChat(@RequestParam String userId) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        if(!userOptional.get().getChatsUser2().isEmpty()) {
            List<Chat> chats = userOptional.get().getChatsUser2();
            for (int i = 0; i < chats.size(); i++) {
                if (String.valueOf(chats.get(i).getUser1().getId()).equals(userId)) {
                    return ResponseEntity.ok().body(chats.get(i).getMessages().stream().map(
                            message -> MessageForChatDto.builder()
                                    .name(message.getSender().getNickname())
//                                    .name(userOptional.get().getId().equals(message.getRecipient().getId())
//                                            ? message.getRecipient().getNickname() : message.getSender().getNickname())
                                    .localDateTime(message.getTime())
                                    .message(message.getMessage())
                                    .build()
                    ));
                }
            }
        } else {
            List<Chat> chats = userOptional.get().getChats();
            for (int i = 0; i < chats.size(); i++) {
                if (String.valueOf(chats.get(i).getUser1().getId()).equals(userId)) {
                    return ResponseEntity.ok().body(chats.get(i).getMessages().stream().map(
                            message -> MessageForChatDto.builder()
                                    .name(userOptional.get().getId().equals(message.getSender().getId())
                                            ? message.getSender().getNickname() : message.getRecipient().getNickname())
                                    .localDateTime(message.getTime())
                                    .message(message.getMessage())
                                    .build()
                    ));
                }
            }
        }

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "chat with current user not open"));
    }

    @Override
    public ResponseEntity<?> getSome() {
        return productService.getAllProducts();
    }
}
