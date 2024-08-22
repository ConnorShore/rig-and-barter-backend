package com.rigandbarter.eventlibrary.events;

import com.rigandbarter.core.models.UserBillingInfo;
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
public class BillingInfoUpdatedEvent extends RBEvent {
    private UserBillingInfo billingInfo;
}