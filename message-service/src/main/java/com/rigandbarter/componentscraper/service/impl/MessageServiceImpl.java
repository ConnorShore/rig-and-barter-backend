package com.rigandbarter.componentscraper.service.impl;

import com.rigandbarter.componentscraper.dto.MessageGroupRequest;
import com.rigandbarter.componentscraper.dto.MessageGroupResponse;
import com.rigandbarter.componentscraper.dto.MessageRequest;
import com.rigandbarter.componentscraper.dto.MessageResponse;
import com.rigandbarter.componentscraper.model.Message;
import com.rigandbarter.componentscraper.model.MessageGroup;
import com.rigandbarter.componentscraper.repository.document.IMessageRepository;
import com.rigandbarter.componentscraper.repository.mapper.MessageMapper;
import com.rigandbarter.componentscraper.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private final IMessageRepository messageRepository;

    @Override
    public MessageGroupResponse createMessageGroup(MessageGroupRequest messageGroupRequest) {
        // TODO: Make call to auth service to get names for buyer and seller
        MessageGroup createdGroup = this.messageRepository.saveMessageGroup(MessageMapper.messageGroupDtoToEntity(messageGroupRequest));
        return MessageMapper.messageGroupEntityToDto(createdGroup);
    }

    @Override
    public MessageResponse createMessage(MessageRequest messageRequest) {
        MessageGroup group = this.messageRepository.getMessageGroupById(messageRequest.getGroupId());
        Message createdMessage = this.messageRepository.saveMessageToGroup(messageRequest.getGroupId(), MessageMapper.messageDtoToEntity(messageRequest));
        return MessageMapper.messageEntityToDto(createdMessage, group.getGroupName());
    }

    @Override
    public List<MessageGroupResponse> getAllMessageGroupsForUser(String userId) {
        List<MessageGroup> groups = this.messageRepository.getAllMessageGroupsForUser(userId);
        return groups.stream()
                .map(MessageMapper::messageGroupEntityToDto)
                .toList();
    }

    @Override
    public MessageGroupResponse getMessageGroupForUser(String groupId) {
        MessageGroup group = this.messageRepository.getMessageGroupById(groupId);
        if(group == null)
            throw new NotFoundException("Group with id: " + groupId + " does not exist");

        return MessageMapper.messageGroupEntityToDto(group);
    }
}
