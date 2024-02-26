package com.rigandbarter.listingservice.model;

import com.rigandbarter.eventservice.model.RBEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEvent extends RBEvent {
    private String additionalInfo;
}
