package com.rigandbarter.pcbuilderservice.dto;

import com.rigandbarter.core.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PCBuildRequest {
    private String id;
    private String name;
    private CaseComponentResponse caseComponent;
    private MotherboardComponentResponse motherboardComponent;
    private PowerSupplyComponentResponse powerSupplyComponent;
    private ProcessorComponentResponse cpuComponent;
    private VideoCardComponentResponse gpuComponent;
    private List<MemoryComponentResponse> memoryComponents;
    private List<HardDriveComponentResponse> hardDriveComponents;
    private List<SolidStateDriveComponentResponse> solidStateDriveComponents;
}
