package com.bbc.zuber.model.message.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateMessageCommand {

    @NotNull(message = "SENDER_UUID_NOT_NULL")
    private UUID senderUuid;
    @NotBlank(message = "CONTENT_NOT_BLANK")
    private String content;
}
