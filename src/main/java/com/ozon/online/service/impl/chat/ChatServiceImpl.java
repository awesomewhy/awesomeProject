package com.ozon.online.service.impl.chat;

import com.ozon.online.dto.chat.MessageDto;
import com.ozon.online.dto.chat.MessageForChatDto;
import com.ozon.online.entity.Chat;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.exception.UserNotAuthException;
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
import org.eclipse.angus.mail.imap.protocol.INTERNALDATE;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public ResponseEntity<?> sendMessage(@RequestParam String userId, @RequestBody MessageDto messageDto) throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );
        User companionOptional = userRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "companion not found")
        );

        if (UUID.fromString(userId).equals(user.getId())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "you can't send a message to yourself"));
        }

        Optional<Chat> chatOptional = chatRepository.findChatByUserIds(companionOptional.getId(), user.getId());
        List<MessageForChatDto> messages;

        if (chatOptional.isEmpty()) {
            Chat chat = messageMapper.mapMessageToChatIfNotExistsAndSave(
                    messageDto, user, companionOptional);

            messages = chat.getMessages().stream().map(
                            messageMapper::mapMessageFromChatToMessageForChatDto)
                    .toList();
        } else {
            messageMapper.mapMessageFromChatToMessageForChatDto(
                    chatOptional.get().getMessages(), chatOptional.get(), user, messageDto);

            messages = chatOptional.get().getMessages().stream().map(
                            messageMapper::mapMessageFromChatToMessageForChatDto)
                    .toList();
        }
        return ResponseEntity.ok().body(messages);
    }

    @Override
    public ResponseEntity<?> getMyChats() throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );
        //qwe
        List<Chat> chats = user.getChats();
        if (chats.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no chats"));
        }
        if (chats.get(0).getMessages().isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "no message"));
        }

        return ResponseEntity.ok().body(user.getChats().stream().map(
                chat -> messageMapper.mapChatToChatDto(chat, user)
        ));
    }

    @Override
    public ResponseEntity<?> openChat(@RequestParam String userNickname) throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );

        if (userNickname.equals(user.getNickname())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "you can't send a message to yourself"));
        }

        Chat chatOptional = user.getChats().stream()
                .filter(chat -> chat.getParticipants().stream()
                        .anyMatch(participant -> participant.getNickname().equals(userNickname)))
                .findFirst()
                .orElseThrow(
                        () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "chat not found")
                );
        return ResponseEntity.ok().body(messageMapper.mapMessageFromChatToSortedByTimeMessageForChatDto(chatOptional.getMessages()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteChatById(@RequestParam Long chatId) throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );
        chatRepository.deleteById(chatId);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "chat deleted"));
    }
}
