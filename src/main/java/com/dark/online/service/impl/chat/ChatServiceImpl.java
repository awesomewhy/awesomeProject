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
import com.dark.online.repository.MessageRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ChatService;
import com.dark.online.service.ImageService;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ProductService productService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ImageService imageService;

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

//        Optional<Chat> chatOptional = chatRepository.findChatByUserIds(userOptional.get().getId(), companionOptional.get().getId());

        Optional<Chat> chatOptional = chatRepository.findChatByUserIds(companionOptional.get().getId(), userOptional.get().getId());
        if (chatOptional.isEmpty()) {
            Chat chat = Chat.builder()
                    .messages(new ArrayList<>())
//                    .senderId(userOptional.get())
//                    .companionId(companionOptional.get())
                    .images(new ArrayList<>())
                    .localDateTime(LocalDateTime.now())
                    .participants(new ArrayList<>())
                    .build();
            chat.getParticipants().add(userOptional.get());
            chat.getParticipants().add(companionOptional.get());
            chat.getMessages().add(Message.builder()
                    .chat(chat)
                    .sender(userOptional.get())
//                    .recipient(companionOptional.get())
                    .message(messageDto.getMessage())
                    .messageStatus(MessageStatus.DELIVERED)
                    .time(LocalDateTime.now())
                    .build());
            chatRepository.save(chat);
            return ResponseEntity.ok().body(chat.getMessages().stream().map(
                    message -> MessageForChatDto.builder()
                            .name(message.getSender().getNickname())
//                            .name(userOptional.get().getId().equals(message.getSender().getId())
//                                    ? message.getSender().getNickname() : message.getRecipient().getNickname())
                            .localDateTime(message.getTime())
                            .message(message.getMessage())
                            .build()
            ));
        } else {
            List<Message> messageUserOptional = chatOptional.get().getMessages();
            messageUserOptional.add(Message.builder()
                    .chat(chatOptional.get())
                    .sender(userOptional.get())
//                    .recipient(companionOptional.get())
                    .message(messageDto.getMessage())
                    .messageStatus(MessageStatus.DELIVERED)
                    .time(LocalDateTime.now())
                    .build());
            return ResponseEntity.ok().body(chatOptional.get().getMessages().stream().map(
                    message -> MessageForChatDto.builder()
                            .name(message.getSender().getNickname())
//                            .name(userOptional.get().getId().equals(message.getSender().getId())
//                                    ? message.getSender().getNickname() : message.getRecipient().getNickname())
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
                                .chatId(chat.getId())
                                .messageType(MessageStatus.DELIVERED)
                                .companionName(chat.getParticipants().get(0).getId().equals(userOptional.get().getId())
                                        ? chat.getParticipants().get(1).getNickname() : chat.getParticipants().get(0).getNickname())

                                .companionId(String.valueOf(chat.getParticipants().get(0).getId().equals(userOptional.get().getId())
                                        ? chat.getParticipants().get(1).getId() : chat.getParticipants().get(0).getId()))
                                .time(chat.getLocalDateTime())
                                .lastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getMessage())
                                .build()
                ));
    }
    @Override
    public ResponseEntity<?> openChat(@RequestParam Long chatId) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
//                -> new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"), HttpStatus.NOT_FOUND)));

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        if (userOptional.get().getChats().stream().noneMatch(chat -> chat.getId().equals(chatId))) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "chat not found"));
        }

        Optional<Chat> chat = chatRepository.findById(chatId); // переделать в поиск только в своих чатах

        if(chat.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "chat not found"));
        }

        return ResponseEntity.ok().body(chat.get().getMessages().stream().map(
                        message -> MessageForChatDto.builder()
                                .name(message.getSender().getNickname())
//                                .name(userOptional.get().getId().equals(message.getSender().getId())
//                                        ? message.getSender().getNickname() : message.getRecipient().getNickname())
                                .localDateTime(message.getTime())
                                .message(message.getMessage())
                                .build()));
//        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
//        }
//        List<Chat> chats = userOptional.get().getChats();
//        for (int i = 0; i < chats.size(); i++) {
//            if (chats.get(i).getId().equals(chatId)) {
//                return ResponseEntity.ok().body(chats.get(i).getMessages().stream().map(
//                        message -> MessageForChatDto.builder()
//                                .name(message.getSender().getNickname())
////                                .name(userOptional.get().getId().equals(message.getRecipient().getId())
////                                        ? message.getRecipient().getNickname() : message.getSender().getNickname())
//                                .localDateTime(message.getTime())
//                                .message(message.getMessage())
//                                .build()
//                ));
//            }
//        }
//        if(!userOptional.get().getChatsUser2().isEmpty()) {
//            List<Chat> chats = userOptional.get().getChatsUser2();
//            for (int i = 0; i < chats.size(); i++) {
//                if (String.valueOf(chats.get(i).getUser1().getId()).equals(userId)) {
//                    return ResponseEntity.ok().body(chats.get(i).getMessages().stream().map(
//                            message -> MessageForChatDto.builder()
//                                    .name(message.getSender().getNickname())
////                                    .name(userOptional.get().getId().equals(message.getRecipient().getId())
////                                            ? message.getRecipient().getNickname() : message.getSender().getNickname())
//                                    .localDateTime(message.getTime())
//                                    .message(message.getMessage())
//                                    .build()
//                    ));
//                }
//            }
//        } else {
//            }

//        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "chat with current user not open"));
    }

    @Override
    public ResponseEntity<?> getSome() {
        return productService.getAllProducts();
    }

//    private Chat getMappedChat(Chat chat, User userOptional, User companionOptional, MessageDto messageDto) {
//        Chat chat2 = Chat.builder()
//                .user1(companionOptional)
//                .user2(userOptional)
//                .messages(new ArrayList<>())
//                .images(new ArrayList<>())
//                .build();
//        chat.getMessages().add(
//                Message.builder()
//                        .chat(chat)
//                        .message(messageDto.getMessage())
//                        .sender(companionOptional)
//                        .recipient(userOptional)
//                        .messageStatus(MessageStatus.DELIVERED)
//                        .time(LocalDateTime.now())
//                        .build());
//        return chat;
//    }
}
