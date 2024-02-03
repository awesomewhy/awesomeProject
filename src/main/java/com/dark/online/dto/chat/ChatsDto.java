package com.dark.online.dto.chat;

import com.dark.online.enums.MessageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ChatsDto {
    private String companionName;
    private String companionId;
    private String lastMessage;
    private LocalDateTime time;
    private MessageStatus messageType;
    private byte[] avatar;
}
