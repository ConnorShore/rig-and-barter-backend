package com.rigandbarter.notificationservice.model.notification;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotification extends Notification {
    private String subject;
    private String body;

    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL_NOTIFICATION;
    }
}
