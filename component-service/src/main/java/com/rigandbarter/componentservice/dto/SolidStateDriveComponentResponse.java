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
public class SolidStateDriveComponentResponse extends ComponentResponse {
    private String formFactor;
    private String protocol;
    private int size;       //in gb
}
