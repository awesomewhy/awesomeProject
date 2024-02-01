package com.dark.online.dto.chat;

import com.dark.online.enums.MessageStatus;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ChatsDto {
    private String companionName;
    private String message;
    private MessageStatus messageType;
    private byte[] avatar;
}
