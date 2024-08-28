package com.rigandbarter.messageservice.controller.impl;

import com.rigandbarter.messageservice.controller.IMessagingController;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessagingControllerImpl implements IMessagingController {
    private final IMessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void handleMessage(MessageRequest messageRequest) {
        MessageResponse messageResponse = messageService.createMessage(messageRequest);
        messagingTemplate.convertAndSendToUser(
                messageRequest.getReceiverId(),
                "/queue/messages",
                messageResponse
        );
    }
}
