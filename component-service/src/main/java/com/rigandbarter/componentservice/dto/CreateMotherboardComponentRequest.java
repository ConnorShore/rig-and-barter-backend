package com.rigandbarter.componentservice.dto;

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
public class CreateMotherboardComponentRequest extends CreateComponentRequest {
    private String connector;
    private String socket;
    private String memoryType;
    private int memoryCapacity;
    private int memorySlots;
}
