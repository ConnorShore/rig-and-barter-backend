package com.rigandbarter.notificationservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/notification")
@Slf4j
public class NotificationController {

    @GetMapping("status")
    public String healthCheck() {
        return "Notification service is running...";
    }
}
