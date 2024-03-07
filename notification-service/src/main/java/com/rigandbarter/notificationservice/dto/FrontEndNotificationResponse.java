package com.rigandbarter.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrontEndNotificationResponse {
    private String id;
    private String title;
    private String body;
    private String actionUrl;
    private boolean seenByUser;
    private LocalDateTime creationDate;
}
