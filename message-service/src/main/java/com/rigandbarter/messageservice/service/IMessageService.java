package com.rigandbarter.messageservice.service;

import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;

import java.util.List;

public interface IMessageService {

    /**
     * Creates a message group to hold chats between buyer and seller
     * @param messageGroupRequest The request info for the message group
     * @return The created message group
     */
    MessageGroupResponse createMessageGroup(MessageGroupRequest messageGroupRequest);

    /**
     * Creates a message for the given group
     * @param messageRequest The message request info
     * @return The created message
     */
    MessageResponse createMessage(MessageRequest messageRequest);

    /**
     * Gets all message groups for the given user
     * @param userId The id of the user to get the message groups for
     * @return All the message groups for the given user
     */
    List<MessageGroupResponse> getAllMessageGroupsForUser(String userId);

    /**
     * Get the message group with the specified id
     * @param groupId The id of the message group to get
     * @return The message group
     */
    MessageGroupResponse getMessageGroupForUser(String groupId);
}
