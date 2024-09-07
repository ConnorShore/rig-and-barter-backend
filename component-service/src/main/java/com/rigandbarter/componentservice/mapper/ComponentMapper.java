package com.rigandbarter.componentservice.mapper;

import com.rigandbarter.componentservice.model.*;
import com.rigandbarter.core.models.*;

/**
 * Mapper for all component types
 */
public class ComponentMapper {

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

    public static HardDriveComponentResponse entitytoDto(HardDriveComponent entity) {
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
