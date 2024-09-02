package com.rigandbarter.messageservice.componentdbservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CaseComponent extends Component {
    private String color;
    private boolean windowed;
    private String motherboardType;
    private String powerSupplyType;
    private int gpuLength;
}
