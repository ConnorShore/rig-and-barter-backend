package com.rigandbarter.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "MessageGroup")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageGroup {
    @Id
    private String id;
    private String buyerId;
    private String sellerId;
    private String groupName;
    private String groupImageUrl;
    private List<Message> messages;

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void deleteMessage(String messageId) {
        messages.removeIf(m -> m.getId().equals(messageId));
    }
}
