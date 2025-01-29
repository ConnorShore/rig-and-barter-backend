package com.rigandbarter.componentservice.mapper;

import com.rigandbarter.componentservice.dto.*;
import com.rigandbarter.componentservice.model.*;
import com.rigandbarter.core.models.*;

/**
 * Mapper for all component types
 */
public class ComponentMapper {

    public static Component dtoToEntity(CreateComponentRequest dto, String id, String imageUrl) {
        return Component.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .build();
    }

    public static CaseComponent dtoToEntity(CreateCaseComponentRequest dto, String id, String imageUrl) {
        return CaseComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .color(dto.getColor())
                .gpuLength(dto.getGpuLength())
                .powerSupplyType(dto.getPowerSupplyType())
                .motherboardType(dto.getMotherboardType())
                .build();
    }

    public static HardDriveComponent dtoToEntity(CreateHardDriveComponentRequest dto, String id, String imageUrl) {
        return HardDriveComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .size(dto.getSize())
                .rpm(dto.getRpm())
                .cacheSize(dto.getCacheSize())
                .build();
    }

    public static MemoryComponent dtoToEntity(CreateMemoryComponentRequest dto, String id, String imageUrl) {
        return MemoryComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .size(dto.getSize())
                .clockSpeed(dto.getClockSpeed())
                .numSticks(dto.getNumSticks())
                .build();
    }

    public static MotherboardComponent dtoToEntity(CreateMotherboardComponentRequest dto, String id, String imageUrl) {
        return MotherboardComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .connector(dto.getConnector())
                .socket(dto.getSocket())
                .memoryCapacity(dto.getMemoryCapacity())
                .memorySlots(dto.getMemorySlots())
                .memoryType(dto.getMemoryType())
                .build();
    }

    public static PowerSupplyComponent dtoToEntity(CreatePowerSupplyComponentRequest dto, String id, String imageUrl) {
        return PowerSupplyComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .watts(dto.getWatts())
                .connector(dto.getConnector())
                .num6PinPCIE(dto.getNum6PinPCIE())
                .num8PinPCIE(dto.getNum8PinPCIE())
                .build();
    }

    public static ProcessorComponent dtoToEntity(CreateProcessorComponentRequest dto, String id, String imageUrl) {
        return ProcessorComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .cores(dto.getCores())
                .threads(dto.getThreads())
                .baseClock(dto.getBaseClock())
                .turboClock(dto.getTurboClock())
                .build();
    }

    public static SolidStateDriveComponent dtoToEntity(CreateSolidStateDriveComponentRequest dto, String id, String imageUrl) {
        return SolidStateDriveComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .formFactor(dto.getFormFactor())
                .protocol(dto.getProtocol())
                .size(dto.getSize())
                .build();
    }

    public static VideoCardComponent dtoToEntity(CreateVideoCardComponentRequest dto, String id, String imageUrl) {
        return VideoCardComponent.builder()
                .id(id)
                .name(dto.getName())
                .imageUrl(imageUrl)
                .category(dto.getComponentCategory())
                .manufacturer(dto.getManufacturer())
                .length(dto.getLength())
                .boostClock(dto.getBoostClock())
                .slots(dto.getSlots())
                .vram(dto.getVram())
                .numHDMIs(dto.getNumHDMIs())
                .numDisplayPorts(dto.getNumDisplayPorts())
                .build();
    }

    public static ComponentResponse entityToDto(Component entity) {
        return ComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .build();
    }

    public static CaseComponentResponse entityToDto(CaseComponent entity) {
        return CaseComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .color(entity.getColor())
                .gpuLength(entity.getGpuLength())
                .powerSupplyType(entity.getPowerSupplyType())
                .motherboardType(entity.getMotherboardType())
                .build();
    }

    public static HardDriveComponentResponse entityToDto(HardDriveComponent entity) {
        return HardDriveComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .size(entity.getSize())
                .rpm(entity.getRpm())
                .cacheSize(entity.getCacheSize())
                .build();
    }

    public static MemoryComponentResponse entityToDto(MemoryComponent entity) {
        return MemoryComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .size(entity.getSize())
                .clockSpeed(entity.getClockSpeed())
                .numSticks(entity.getNumSticks())
                .build();
    }

    public static MotherboardComponentResponse entityToDto(MotherboardComponent entity) {
        return MotherboardComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .connector(entity.getConnector())
                .socket(entity.getSocket())
                .memoryCapacity(entity.getMemoryCapacity())
                .memorySlots(entity.getMemorySlots())
                .memoryType(entity.getMemoryType())
                .build();
    }

    public static PowerSupplyComponentResponse entityToDto(PowerSupplyComponent entity) {
        return PowerSupplyComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .watts(entity.getWatts())
                .connector(entity.getConnector())
                .num6PinPCIE(entity.getNum6PinPCIE())
                .num8PinPCIE(entity.getNum8PinPCIE())
                .build();
    }

    public static ProcessorComponentResponse entityToDto(ProcessorComponent entity) {
        return ProcessorComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .cores(entity.getCores())
                .threads(entity.getThreads())
                .baseClock(entity.getBaseClock())
                .turboClock(entity.getTurboClock())
                .build();
    }

    public static SolidStateDriveComponentResponse entityToDto(SolidStateDriveComponent entity) {
        return SolidStateDriveComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .formFactor(entity.getFormFactor())
                .protocol(entity.getProtocol())
                .size(entity.getSize())
                .build();
    }

    public static VideoCardComponentResponse entityToDto(VideoCardComponent entity) {
        return VideoCardComponentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .manufacturer(entity.getManufacturer())
                .length(entity.getLength())
                .boostClock(entity.getBoostClock())
                .slots(entity.getSlots())
                .vram(entity.getVram())
                .numHDMIs(entity.getNumHDMIs())
                .numDisplayPorts(entity.getNumDisplayPorts())
                .build();
    }
}
