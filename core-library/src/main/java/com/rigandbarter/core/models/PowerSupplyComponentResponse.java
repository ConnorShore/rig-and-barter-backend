package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PowerSupplyComponentResponse extends ComponentResponse {
    private String connector;
    private int watts;
    private int num8PinPCIE;
    private int num6PinPCIE;
}
