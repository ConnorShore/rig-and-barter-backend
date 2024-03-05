package com.rigandbarter.notificationservice.model.notification;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "FrontEndNotification")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FrontEndNotification extends Notification {
    private String title;
    private String body;
    private String actionUrl;
    private boolean seenByUser;
    private FrontEndNotificationType notificationType;

    @Override
    public NotificationType getType() {
        return NotificationType.FRONT_END_NOTIFICATION;
    }
}
