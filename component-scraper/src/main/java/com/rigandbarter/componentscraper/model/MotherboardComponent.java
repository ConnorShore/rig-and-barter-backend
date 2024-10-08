package com.rigandbarter.componentscraper.model;

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
    private String connector;
    private String socket;
    private String memoryType;
    private int memoryCapacity;
    private int memorySlots;
}
