package com.rigandbarter.notificationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("api/notification")
public interface INotificationController {

    /**
     * Status endpoint to see if the service is running
     */
    @GetMapping("status")
    String healthCheck();
}
