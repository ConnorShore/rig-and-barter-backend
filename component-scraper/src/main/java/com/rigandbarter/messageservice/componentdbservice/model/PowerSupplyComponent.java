package com.rigandbarter.messageservice.componentdbservice.model;

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
public class PowerSupplyComponent extends Component {
    private String connector;
    private int watts;
    private int num8PinPCIE;
    private int num6PinPCIE;
}
