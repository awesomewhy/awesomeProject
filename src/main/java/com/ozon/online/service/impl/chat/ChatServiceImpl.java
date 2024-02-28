package com.ozon.online.service.impl.chat;

import com.ozon.online.dto.chat.MessageDto;
import com.ozon.online.dto.chat.MessageForChatDto;
import com.ozon.online.entity.Chat;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.mapper.MessageMapper;
import com.ozon.online.repository.ChatRepository;
import com.ozon.online.repository.MessageRepository;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.ChatService;
import com.ozon.online.service.ImageService;
import com.ozon.online.service.ProductService;
import com.ozon.online.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
        User userOptional = userService.getAuthenticationPrincipalUserByNickname().orElseThrow();
        User companionOptional = userRepository.findById(UUID.fromString(userId)).orElseThrow();

        if (UUID.fromString(userId).equals(userOptional.getId())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "you can't send a message to yourself"));
        }

        Optional<Chat> chatOptional = chatRepository.findChatByUserIds(companionOptional.getId(), userOptional.getId());
        List<MessageForChatDto> messages;

        if (chatOptional.isPresent()) {
            messageMapper.mapMessageFromChatToMessageForChatDto(
                    chatOptional.get().getMessages(), chatOptional.get(), userOptional, messageDto);
        } else {
            messageMapper.mapMessageToChatIfNotExistsAndSave(
                    messageDto, userOptional, companionOptional);
        }

        messages = chatOptional.get().getMessages().stream().map(
                        messageMapper::mapMessageFromChatToMessageForChatDto)
                .toList();
        return ResponseEntity.ok().body(messages);
    }

    @Override
    public ResponseEntity<?> getMyChats() {
        User userOptional = userService.getAuthenticationPrincipalUserByNickname().orElseThrow();

        List<Chat> chats = userOptional.getChats();
        if (chats.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no chats"));
        }
        if (chats.get(0).getMessages().isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no message"));
        }

        return ResponseEntity.ok().body(userOptional.getChats().stream().map(
                chat -> messageMapper.mapChatToChatDto(chat, userOptional)
        ));
    }

    @Override
    public ResponseEntity<?> openChat(@RequestParam String userNickname) {
        User userOptional = userService.getAuthenticationPrincipalUserByNickname().orElseThrow();

        if (userNickname.equals(userOptional.getNickname())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "you can't send a message to yourself"));
        }

        Optional<Chat> chatOptional = userOptional.getChats().stream()
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
        User userOptional = userService.getAuthenticationPrincipalUserByNickname().orElseThrow();

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
