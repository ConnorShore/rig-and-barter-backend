package com.rigandbarter.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Notification {
    /**
     * TODO: Figure the model out
     */
    protected String id;
    protected String targetUser;
    protected NotificationType type;
}
