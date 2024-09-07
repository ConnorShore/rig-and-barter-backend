package com.rigandbarter.pcbuilderservice.model;

import com.rigandbarter.core.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "PCBuild")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PCBuild {
    private String id;
    private String userId;
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
