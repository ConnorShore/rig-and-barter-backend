package com.rigandbarter.componentscraper.controller.impl;

import com.rigandbarter.componentscraper.controller.IMessagingController;
import com.rigandbarter.componentscraper.dto.MessageRequest;
import com.rigandbarter.componentscraper.dto.MessageResponse;
import com.rigandbarter.componentscraper.service.IMessageService;
import com.rigandbarter.componentscraper.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessagingControllerImpl implements IMessagingController {

    @Value("${rb.websocket.topic.messaging}")
    private String MESSAGING_TOPIC;

    private final IMessageService messageService;
    private final WebSocketService webSocketService;

    @Override
    public void handleMessage(MessageRequest messageRequest) {
        MessageResponse messageResponse = messageService.createMessage(messageRequest);
        webSocketService.sendDirectedFrontendMessage(messageResponse, messageRequest.getGroupId() + "/queue/message");
        webSocketService.sendGeneralFrontendMessage(messageResponse);
    }
}
