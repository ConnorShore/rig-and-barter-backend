package com.rigandbarter.messageservice.controller.impl;

import com.rigandbarter.messageservice.controller.IMessageController;
import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageControllerImpl implements IMessageController {

    private final IMessageService messageService;

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
    public String healthCheck() {
        return "Message service is running...";
    }
}
