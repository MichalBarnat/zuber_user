package com.bbc.zuber.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime dateTime;
}
