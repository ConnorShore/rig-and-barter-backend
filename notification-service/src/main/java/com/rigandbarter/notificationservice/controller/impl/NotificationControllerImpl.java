package com.rigandbarter.notificationservice.controller.impl;

import com.rigandbarter.notificationservice.controller.INotificationController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class NotificationControllerImpl implements INotificationController {

    @Override
    public String healthCheck() {
        return "Notification service is running...";
    }
}
