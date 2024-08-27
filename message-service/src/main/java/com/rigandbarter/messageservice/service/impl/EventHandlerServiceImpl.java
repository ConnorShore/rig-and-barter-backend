package com.rigandbarter.messageservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.service.IEventHandlerService;
import com.rigandbarter.messageservice.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final IMessageService messageService;

    @Override
    public RBResultStatus<Void> handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        MessageGroupRequest messageGroup =  MessageGroupRequest.builder()
                .buyerId(event.getBuyerId())
                .sellerId(event.getSellerId())
                .build();

        MessageGroupResponse createdGroup = messageService.createMessageGroup(messageGroup);
        if(createdGroup == null)
            return new RBResultStatus<>(false, "Failed to create message group");

        return new RBResultStatus<>(true);
    }
}
