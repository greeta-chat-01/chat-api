package com.joejoe2.chat.data.message;

import com.joejoe2.chat.data.UserPublicProfile;
import com.joejoe2.chat.models.PrivateMessage;
import com.joejoe2.chat.utils.TimeUtil;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@NoArgsConstructor
public class PrivateMessageDto extends MessageDto {
  public PrivateMessageDto(PrivateMessage message) {
    super(
        message.getVersion(),
        message.getId(),
        message.getChannel().getId(),
        message.getMessageType(),
        UserPublicProfile.builder()
            .id(message.getFrom().getId().toString())
            .username(message.getFrom().getUserName())
            .build(),
        UserPublicProfile.builder()
            .id(message.getFrom().getId().toString())
            .username(message.getTo().getUserName())
            .build(),
        message.getContent(),
        TimeUtil.roundToMicro(message.getCreateAt()).toString(),
        TimeUtil.roundToMicro(message.getUpdateAt()).toString());
  }
}
