package com.rigandbarter.pcbuilderservice.mapper;

import com.rigandbarter.pcbuilderservice.dto.PCBuildRequest;
import com.rigandbarter.pcbuilderservice.dto.PCBuildResponse;
import com.rigandbarter.pcbuilderservice.model.PCBuild;

import java.util.UUID;

public class PCBuildMapper {

    /**
     * Converts request dto to entity
     * @param dto The dto to convert
     * @param userId The user Id for the pc build
     * @return The entity for the db
     */
    public static PCBuild dtoToEntity(PCBuildRequest dto, String userId) {
        return PCBuild.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .name(dto.getName())
                .caseComponent(dto.getCaseComponent())
                .motherboardComponent(dto.getMotherboardComponent())
                .powerSupplyComponent(dto.getPowerSupplyComponent())
                .cpuComponent(dto.getCpuComponent())
                .gpuComponent(dto.getGpuComponent())
                .memoryComponents(dto.getMemoryComponents())
                .hardDriveComponents(dto.getHardDriveComponents())
                .solidStateDriveComponents(dto.getSolidStateDriveComponents())
                .build();
    }

    /**
     * Converts the db entity to a resposne dto
     * @param entity The entity to convert
     * @return The converted entity as a dto response
     */
    public static PCBuildResponse entityToDto(PCBuild entity) {
        return PCBuildResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .caseComponent(entity.getCaseComponent())
                .motherboardComponent(entity.getMotherboardComponent())
                .powerSupplyComponent(entity.getPowerSupplyComponent())
                .cpuComponent(entity.getCpuComponent())
                .gpuComponent(entity.getGpuComponent())
                .memoryComponents(entity.getMemoryComponents())
                .hardDriveComponents(entity.getHardDriveComponents())
                .solidStateDriveComponents(entity.getSolidStateDriveComponents())
                .build();
    }
}
