package com.rigandbarter.messageservice.repository.document;

import com.rigandbarter.messageservice.model.Message;
import com.rigandbarter.messageservice.model.MessageGroup;

import java.util.List;

public interface IMessageRepository {

    MessageGroup saveMessageGroup(MessageGroup group);

    Message saveMessageToGroup(String groupId, Message message);

    void deleteMessageFromGroup(String groupId, String messageId);

    void deleteNotificationGroup(String groupId);

    void deleteMessagesForUser(String userId);

    MessageGroup getMessageGroupById(String groupId);

    List<MessageGroup> getAllMessageGroupsForUser(String userId);
}
