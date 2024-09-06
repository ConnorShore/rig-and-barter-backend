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
public class ProcessorComponentResponse extends ComponentResponse {
    private double baseClock;
    private double turboClock;
    private int cores;
    private int threads;
}
