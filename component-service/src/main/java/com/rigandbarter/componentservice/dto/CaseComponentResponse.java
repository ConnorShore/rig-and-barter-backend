package com.rigandbarter.componentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CaseComponentResponse extends ComponentResponse {
    private String color;
    private boolean windowed;
    private String motherboardType;
    private String powerSupplyType;
    private int gpuLength;
}
