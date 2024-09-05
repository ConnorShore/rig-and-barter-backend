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
                .caseId(dto.getCaseId())
                .motherBoardId(dto.getMotherBoardId())
                .powerSupplyId(dto.getPowerSupplyId())
                .cpuId(dto.getCpuId())
                .gpuId(dto.getGpuId())
                .memoryIds(dto.getMemoryIds())
                .hardDriveIds(dto.getHardDriveIds())
                .solidStateDriveIds(dto.getSolidStateDriveIds())
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
                .userId(entity.getUserId())
                .name(entity.getName())
                .caseId(entity.getCaseId())
                .motherBoardId(entity.getMotherBoardId())
                .powerSupplyId(entity.getPowerSupplyId())
                .cpuId(entity.getCpuId())
                .gpuId(entity.getGpuId())
                .memoryIds(entity.getMemoryIds())
                .hardDriveIds(entity.getHardDriveIds())
                .solidStateDriveIds(entity.getSolidStateDriveIds())
                .build();
    }
}
