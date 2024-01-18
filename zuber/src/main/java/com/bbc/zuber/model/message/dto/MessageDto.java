package com.bbc.zuber.model.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MessageDto {

    private UUID messageUuid;
    private UUID senderUuid;
    private String senderName;
    private String senderSurname;
    private String content;
    private LocalDateTime dateTime;
}
