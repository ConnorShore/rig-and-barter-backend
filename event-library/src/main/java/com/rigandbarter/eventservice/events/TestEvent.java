package com.rigandbarter.eventservice.events;

import com.rigandbarter.eventservice.model.RBEvent;
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
public class TestEvent extends RBEvent {
    private String additionalInfo;
}
