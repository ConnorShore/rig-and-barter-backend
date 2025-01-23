package com.rigandbarter.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String id;
    private String groupId;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime timestamp;
}
