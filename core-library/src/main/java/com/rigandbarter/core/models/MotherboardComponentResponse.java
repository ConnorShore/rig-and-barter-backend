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
public class MotherboardComponentResponse extends ComponentResponse {
    private String connector;
    private String socket;
    private String memoryType;
    private int memoryCapacity;
    private int memorySlots;
}
