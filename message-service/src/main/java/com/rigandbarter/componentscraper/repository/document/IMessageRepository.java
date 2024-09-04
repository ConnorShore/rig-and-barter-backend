package com.rigandbarter.componentscraper.repository.document;

import com.rigandbarter.componentscraper.model.Message;
import com.rigandbarter.componentscraper.model.MessageGroup;

import java.util.List;

public interface IMessageRepository {

    MessageGroup saveMessageGroup(MessageGroup group);

    Message saveMessageToGroup(String groupId, Message message);

    void deleteMessageFromGroup(String groupId, String messageId);

    void deleteNotificationGroup(String groupId);

    MessageGroup getMessageGroupById(String groupId);

    List<MessageGroup> getAllMessageGroupsForUser(String userId);
}
