package com.rigandbarter.notificationservice.controller;

import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("api/notification")
public interface INotificationController {

    /**
     * Gets all the notifications for the current user
     * @param principal The principal auth for the user to get notifications for
     * @return The list of notifications for the current user
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<FrontEndNotificationResponse> getNotificationsForUser(@AuthenticationPrincipal Jwt principal);

    /**
     * Deletes the specified notifications from the db
     * @param notificationIds The list of ids for notification to remove
     */
    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    void deleteNotifications(@RequestParam("ids") List<String> notificationIds);

    /**
     * Marks a notificaiton as seen
     * @param notificationId The notificaiton to mark as seen
     */
    @PatchMapping("{notificationId}/seen")
    @ResponseStatus(HttpStatus.OK)
    void markNotificationAsSeen(@PathVariable String notificationId);


    /**
     * Marks all user's notifications as seen
     * @param principal The auth principal of the user for whom to mark notifications as seen for
     */
    @PatchMapping("/seen")
    @ResponseStatus(HttpStatus.OK)
    void markAllUserNotificationAsSeen(@AuthenticationPrincipal Jwt principal);

    /**
     * Status endpoint to see if the service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
