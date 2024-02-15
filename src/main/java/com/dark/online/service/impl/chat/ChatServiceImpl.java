package com.dark.online.service.impl.chat;

import com.dark.online.dto.chat.ChatsDto;
import com.dark.online.dto.chat.MessageDto;
import com.dark.online.dto.chat.MessageForChatDto;
import com.dark.online.entity.Chat;
import com.dark.online.entity.Message;
import com.dark.online.entity.User;
import com.dark.online.enums.MessageStatus;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.mapper.MessageMapper;
import com.dark.online.repository.ChatRepository;
import com.dark.online.repository.MessageRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.ChatService;
import com.dark.online.service.ImageService;
import com.dark.online.service.ProductService;
import com.dark.online.service.UserService;
import com.dark.online.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.sql.Timestamp;
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
    private final MessageMapper messageMapper;

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
        if (UUID.fromString(userId).equals(userOptional.get().getId())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "you can't send a message to yourself"));
        }

        Optional<Chat> chatOptional = chatRepository.findChatByUserIds(companionOptional.get().getId(), userOptional.get().getId());
        List<MessageForChatDto> messages;

        if (chatOptional.isEmpty()) {
            Chat chat = messageMapper.mapMessageToChatIfNotExistsAndSave(
                    messageDto, userOptional.get(), companionOptional.get());

            messages = chat.getMessages().stream().map(
                    messageMapper::mapMessageFromChatToMessageForChatDto)
                    .toList();
//            return ResponseEntity.ok().body(chat.getMessages().stream().map(
//                    messageMapper::mapMessageFromChatToMessageForChatDto
//            ));

        } else {
            messageMapper.mapMessageFromChatToMessageForChatDto(
                    chatOptional.get().getMessages(), chatOptional.get(), userOptional.get(), messageDto);

            messages = chatOptional.get().getMessages().stream().map(
                            messageMapper::mapMessageFromChatToMessageForChatDto)
                    .toList();
//            return ResponseEntity.ok().body(chatOptional.get().getMessages().stream().map(
//                    messageMapper::mapMessageFromChatToMessageForChatDto
//            ));

        }
        return ResponseEntity.ok().body(messages);

    }

    @Override
    public ResponseEntity<?> getMyChats() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        List<Chat> chats = userOptional.get().getChats();
        if (chats.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no chats"));
        }
        if (chats.get(0).getMessages().isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no message"));
        }

        return ResponseEntity.ok().body(userOptional.get().getChats().stream().map(
                        chat -> messageMapper.mapChatToChatDto(chat, userOptional.get())
                ));
    }

    @Override
    public ResponseEntity<?> openChat(@RequestParam String userNickname) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        if (userNickname.equals(userOptional.get().getNickname())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "you can't send a message to yourself"));
        }

        Optional<Chat> chatOptional = userOptional.get().getChats().stream()
                .filter(chat -> chat.getParticipants().stream()
                        .anyMatch(participant -> participant.getNickname().equals(userNickname)))
                .findFirst();

        if (chatOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "chat not found"));
        }

        return ResponseEntity.ok().body(messageMapper.mapMessageFromChatToSortedByTimeMessageForChatDto
                (chatOptional.get().getMessages()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteChatById(@RequestParam Long chatId) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }

        chatRepository.deleteById(chatId);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "chat deleted"));
    }


    // поиск чта по айди
//    @Override
//    public ResponseEntity<?> openChat(@RequestParam Long chatId) {
//        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
//
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
//        }
//
//        Optional<Chat> chatOptional = userOptional.get().getChats().stream()
//                .filter(chat -> chat.getId().equals(chatId))
//                .findFirst();
//
//        if(chatOptional.isEmpty()) {
//            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "chat not found"));
//        }
//
//        return ResponseEntity.ok().body(chatOptional.get().getMessages().stream()
//                .sorted(Comparator.comparing(Message::getTime)).map(
//                        message -> MessageForChatDto.builder()
//                                .name(message.getSender().getNickname())
//                                .localDateTime(message.getTime())
//                                .message(message.getMessage())
//                                .avatar(message.getSender().getAvatarId().getImageData())
//                                .build()));
//    }
}
