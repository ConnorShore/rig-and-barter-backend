package com.rigandbarter.messageservice.controller;


import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/message")
public interface IMessageController {

    /**
     * Creates a message group for the buyer and seller
     * @param messageGroupRequest The info to create the message group
     * @return The created message group
     */
    @PostMapping("group")
    @ResponseStatus(HttpStatus.CREATED)
    MessageGroupResponse createMessageGroup(@RequestBody MessageGroupRequest messageGroupRequest);

    /**
     * Creates a message for the given group
     * @param messageRequest The message info
     * @return The created message
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    MessageResponse createMessage(@RequestBody MessageRequest messageRequest);

    /**
     * Gets the message groups for the user
     * @return All the message groups for the given user
     */
    @GetMapping("group")
    @ResponseStatus(HttpStatus.OK)
    List<MessageGroupResponse> getAllMessageGroupsForUser(@AuthenticationPrincipal Jwt principal);

    /**
     * Health check for the message service
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
