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
public class MotherboardComponent extends Component {
    private String type;
    private String socket;
    private String memoryType;
    private int memoryCapacity;
    private int memorySlots;
}
