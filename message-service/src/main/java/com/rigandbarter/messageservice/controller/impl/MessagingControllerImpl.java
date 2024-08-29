package com.rigandbarter.messageservice.controller.impl;

import com.rigandbarter.messageservice.controller.IMessagingController;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.service.IMessageService;
import com.rigandbarter.messageservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
