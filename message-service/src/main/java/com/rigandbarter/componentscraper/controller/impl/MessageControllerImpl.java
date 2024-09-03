package com.rigandbarter.componentscraper.controller.impl;

import com.rigandbarter.componentscraper.controller.IMessageController;
import com.rigandbarter.componentscraper.dto.MessageGroupRequest;
import com.rigandbarter.componentscraper.dto.MessageGroupResponse;
import com.rigandbarter.componentscraper.dto.MessageRequest;
import com.rigandbarter.componentscraper.dto.MessageResponse;
import com.rigandbarter.componentscraper.service.IMessageService;
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
    public MessageGroupResponse getMessageGroupUser(String groupId) {
        return messageService.getMessageGroupForUser(groupId);
    }

    @Override
    public String healthCheck() {
        return "Message service is running...";
    }
}
