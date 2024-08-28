package com.rigandbarter.messageservice.controller.impl;

import com.rigandbarter.messageservice.controller.IMessageController;
import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.service.IMessageService;
import com.rigandbarter.messageservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageControllerImpl implements IMessageController {

    private final IMessageService messageService;
    private final WebSocketService webSocketService;

    @Override
    public MessageGroupResponse createMessageGroup(MessageGroupRequest messageGroupRequest) {
        return messageService.createMessageGroup(messageGroupRequest);
    }

    @Override
    public MessageResponse createMessage(MessageRequest messageRequest) {
        return messageService.createMessage(messageRequest);
    }

    @Override
    public List<MessageGroupResponse> getAllMessageGroupsForUser(Jwt principal) {
        return messageService.getAllMessageGroupsForUser(principal.getSubject());
    }

    @Override
    public MessageGroupResponse getMessageGroupUser(String groupId) {
        return messageService.getMessageGroupForUser(groupId);
    }

    @Override
    public String healthCheck() {
        webSocketService.sendGeneralFrontendMessage(MessageResponse.builder()
                .id("TEST_ID")
                .content("CONTENT")
                .senderId("SENDERID")
                .receiverId("RECEIVERID")
                .timestamp(LocalDateTime.now())
                .build());
        return "Message service is running...";
    }
}
