package com.dark.online.mapper;

import com.dark.online.dto.chat.ChatsDto;
import com.dark.online.dto.chat.MessageDto;
import com.dark.online.dto.chat.MessageForChatDto;
import com.dark.online.entity.Chat;
import com.dark.online.entity.Message;
import com.dark.online.entity.User;
import com.dark.online.enums.MessageStatus;
import com.dark.online.repository.ChatRepository;
import com.dark.online.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final ChatRepository chatRepository;

    public Chat mapMessageToChatIfNotExistsAndSave(
            MessageDto message, User user, User companion) {
        Chat chat = Chat.builder()
                .messages(new ArrayList<>())
                .images(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .participants(new ArrayList<>())
                .build();
        chat.getParticipants().add(user);
        chat.getParticipants().add(companion);
        chat.getMessages().add(Message.builder()
                .chat(chat)
                .sender(user)
                .message(message.getMessage())
                .messageStatus(MessageStatus.DELIVERED)
                .time(LocalDateTime.now())
                .build());
        chatRepository.save(chat);
        return chat;
    }

    public MessageForChatDto mapMessageFromChatToMessageForChatDto(Message message) {
        return MessageForChatDto.builder()
                .name(message.getSender().getNickname())
                .localDateTime(message.getTime())
                .message(message.getMessage())
                .build();
    }

    public void mapMessageFromChatToMessageForChatDto(
            List<Message> message, Chat chat, User user, MessageDto messageDto) {
        message.add(Message.builder()
                .chat(chat)
                .sender(user)
                .message(messageDto.getMessage())
                .messageStatus(MessageStatus.DELIVERED)
                .time(LocalDateTime.now())
                .build());
    }

    public ChatsDto mapChatToChatDto(Chat chat, User user) {
        return ChatsDto.builder()
                .chatId(chat.getId())
                .messageType(MessageStatus.DELIVERED)

                .companionName(chat.getParticipants().get(0).getId().equals(user.getId())
                        ? chat.getParticipants().get(1).getNickname() : chat.getParticipants().get(0).getNickname())

                .companionId(String.valueOf(chat.getParticipants().get(0).getId().equals(user.getId())
                        ? chat.getParticipants().get(1).getId() : chat.getParticipants().get(0).getId()))

                .avatar(ImageUtils.decompressImage(chat.getParticipants().get(0).getId().equals(user.getId())
                        ? chat.getParticipants().get(1).getAvatarId().getImageData() : chat.getParticipants().get(0).getAvatarId().getImageData()))

                .lastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getMessage())

                .build();
    }

    public List<MessageForChatDto> mapMessageFromChatToSortedByTimeMessageForChatDto(List<Message> messages) {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTime))
                .map(message -> MessageForChatDto.builder()
                        .name(message.getSender().getNickname())
                        .localDateTime(message.getTime())
                        .message(message.getMessage())
                        .avatar(message.getSender().getAvatarId().getImageData())
                        .build())
                .toList();
    }

}
