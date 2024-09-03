package com.rigandbarter.componentscraper.dto;

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
    private String groupName;
    private String groupImageUrl;
    private List<MessageResponse> messages;
}
