package com.rigandbarter.notificationservice.service;

import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final INotificationRepository notificationRepository;

}
