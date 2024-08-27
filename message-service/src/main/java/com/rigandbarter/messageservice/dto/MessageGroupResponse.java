package com.rigandbarter.messageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageGroupResponse {
    private String id;
    private String buyerId;
    private String sellerId;
    private List<MessageResponse> messages;
}
