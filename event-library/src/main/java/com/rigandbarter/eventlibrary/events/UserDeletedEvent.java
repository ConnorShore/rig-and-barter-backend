package com.rigandbarter.eventlibrary.events;

import com.rigandbarter.eventlibrary.model.RBEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
public class UserDeletedEvent extends RBEvent {
}
