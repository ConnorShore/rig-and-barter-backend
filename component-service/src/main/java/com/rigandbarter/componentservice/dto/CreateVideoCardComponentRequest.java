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
public class CreateVideoCardComponentRequest extends CreateComponentRequest {
    private double length;
    private double slots;
    private int numHDMIs;
    private int numDisplayPorts;
    private int boostClock;
    private int vram;
}
