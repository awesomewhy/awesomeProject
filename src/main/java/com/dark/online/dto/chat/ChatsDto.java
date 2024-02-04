package com.dark.online.dto.chat;

import com.dark.online.entity.Chat;
import com.dark.online.entity.User;
import com.dark.online.enums.MessageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ChatsDto {
    private Long chatId;
    private String companionName;
    private String companionId;
    private String lastMessage;
    private LocalDateTime time;
    private MessageStatus messageType;
    private byte[] avatar;

//    public static ChatsDto fromT1(Chat chat, User user) {
//        return ChatsDto.builder()
//                .chatId(chat.getId())
//                .messageType(MessageStatus.DELIVERED)
//                .companionName(chat.getParticipants().get(0).getId().equals(user.getId())
//                        ? chat.getParticipants().get(1).getNickname() : chat.getParticipants().get(0).getNickname())
//                .companionId(String.valueOf(chat.getId()))
//                .time(chat.getLocalDateTime())
//                .lastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getMessage())
//                .build();
//    }
//
//    public static ChatsDto fromT2(Chat chat, User user) {
//        return ChatsDto.builder()
//                .chatId(chat.getId())
//                .messageType(MessageStatus.DELIVERED)
//                .companionName(chat.getParticipants().get(0).getId().equals(user.getId())
//                        ? chat.getParticipants().get(1).getNickname() : chat.getParticipants().get(0).getNickname())
//                .companionId(String.valueOf(chat.getId()))
//                .time(chat.getLocalDateTime())
//                .lastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getMessage())
//                .build();
//    }

}
