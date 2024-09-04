package com.rigandbarter.componentservice.dto;

import com.rigandbarter.componentservice.model.Component;
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
public class MemoryComponentResponse extends ComponentResponse {
    private String type;
    private int size;
    private int clockSpeed;
    private int numSticks;
}
