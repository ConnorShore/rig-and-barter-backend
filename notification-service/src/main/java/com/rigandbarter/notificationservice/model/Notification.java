package com.rigandbarter.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "Notification")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Notification {
    /**
     * TODO: Figure the model out
     */
    protected String id;
    protected String eventId;
    protected String targetUser;
    protected LocalDateTime creationDate;
    protected NotificationType type;

    public abstract NotificationType getType();
}
