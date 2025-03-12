package com.rigandbarter.pcbuilderservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
import com.rigandbarter.pcbuilderservice.service.IEventHandlerService;
import com.rigandbarter.pcbuilderservice.service.IPCBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final IPCBuilderService pcBuilderService;

    @Override
    public RBResultStatus<Void> handleUserDeletedEvent(UserDeletedEvent userDeletedEvent) {
        try {
            pcBuilderService.deletePCBuildsByUserId(userDeletedEvent.getUserId());
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }
        return new RBResultStatus<>(true);
    }
}
