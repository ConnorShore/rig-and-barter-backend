package com.rigandbarter.eventlibrary.events;


import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.eventlibrary.model.RBEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent extends RBEvent {
    private UserBasicInfo userInfo;
}
