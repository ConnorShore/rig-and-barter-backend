package com.rigandbarter.pcbuilderservice.model;

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
    private String caseId;
    private String motherBoardId;
    private String powerSupplyId;
    private String cpuId;
    private String gpuId;
    private List<String> memoryIds;
    private List<String> hardDriveIds;
    private List<String> solidStateDriveIds;
}
