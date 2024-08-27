package com.rigandbarter.messageservice.repository.mapper;

import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.model.Message;
import com.rigandbarter.messageservice.model.MessageGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class MessageMapper {

    /**
     * Converts message group dto to entity
     * @param dto the dto to convert to entity
     * @return The created entity
     */
    public static MessageGroup messageGroupDtoToEntity(MessageGroupRequest dto) {
        return MessageGroup.builder()
                .id(UUID.randomUUID().toString())
                .buyerId(dto.getBuyerId())
                .sellerId(dto.getSellerId())
                .messages(new ArrayList<>())
                .build();
    }

    /**
     * Converts message group entity to a dto
     * @param entity The entity to convert to dto
     * @return The created dto
     */
    public static MessageGroupResponse messageGroupEntityToDto(MessageGroup entity) {
        return MessageGroupResponse.builder()
                .id(entity.getId())
                .buyerId(entity.getBuyerId())
                .sellerId(entity.getSellerId())
                .messages(
                        entity.getMessages()
                                .stream()
                                .map(MessageMapper::messageEntityToDto).
                                toList()
                )
                .build();
    }

    /**
     * Converts message entity to dto
     * @param entity The entity to convert to dto
     * @return The created dto
     */
    public static MessageResponse messageEntityToDto(Message entity) {
        return MessageResponse.builder()
                .id(entity.getId())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .build();
    }

    /**
     * Converts message dto to entity
     * @param dto The dto to convert to entity
     * @return The created entity
     */
    public static Message messageDtoToEntity(MessageRequest dto) {
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .content(dto.getContent())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
