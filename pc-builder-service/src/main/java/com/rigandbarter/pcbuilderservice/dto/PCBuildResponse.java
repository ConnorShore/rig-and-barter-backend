package com.rigandbarter.pcbuilderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PCBuildResponse {
    private String id;
    private String name;
    private String caseId;
    private String motherBoardId;
    private String powerSupplyId;
    private String cpuId;
    private String gpuId;
    private List<String> memoryIds;
    private List<String> hardDriveIds;
    private List<String> solidStateDriveIds;
}
