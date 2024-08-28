package com.rigandbarter.messageservice.service.impl;

import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.model.Message;
import com.rigandbarter.messageservice.model.MessageGroup;
import com.rigandbarter.messageservice.repository.document.IMessageRepository;
import com.rigandbarter.messageservice.repository.mapper.MessageMapper;
import com.rigandbarter.messageservice.service.IMessageService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        Message createdMessage = this.messageRepository.saveMessageToGroup(messageRequest.getGroupId(), MessageMapper.messageDtoToEntity(messageRequest));
        return MessageMapper.messageEntityToDto(createdMessage);
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
