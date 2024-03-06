package com.rigandbarter.notificationservice.controller;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("api/notification")
public interface INotificationController {

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<FrontEndNotificationResponse> getNotificationsForUser(@AuthenticationPrincipal Jwt principal);

    /**
     * Delete the notification from the db
     * @param notificationId The notification to remove
     * @param principal The auth principal for current user
     * @return Successful status if deleted, failure status if not
     */
    @DeleteMapping("{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteNotification(@PathVariable String notificationId, @AuthenticationPrincipal Jwt principal);

    /**
     * Status endpoint to see if the service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
