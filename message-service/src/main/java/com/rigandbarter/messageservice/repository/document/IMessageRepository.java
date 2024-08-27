package com.rigandbarter.messageservice.repository.document;

import com.rigandbarter.messageservice.model.Message;
import com.rigandbarter.messageservice.model.MessageGroup;

import java.util.List;

public interface IMessageRepository {

    MessageGroup saveMessageGroup(MessageGroup group);

    Message saveMessageToGroup(String groupId, Message message);

    void deleteMessageFromGroup(String groupId, String messageId);

    void deleteNotificationGroup(String messageGroupId);

    List<MessageGroup> getAllMessageGroupsForUser(String userId);
}
