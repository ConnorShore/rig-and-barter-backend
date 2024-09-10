package com.rigandbarter.pcbuilderservice.service.impl;

import com.rigandbarter.pcbuilderservice.mapper.PCBuildMapper;
import com.rigandbarter.pcbuilderservice.dto.PCBuildRequest;
import com.rigandbarter.pcbuilderservice.dto.PCBuildResponse;
import com.rigandbarter.pcbuilderservice.model.PCBuild;
import com.rigandbarter.pcbuilderservice.repository.IPCBuilderRepository;
import com.rigandbarter.pcbuilderservice.service.IPCBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PCBuilderServiceImpl implements IPCBuilderService {

    private final IPCBuilderRepository pcBuilderRepository;

    @Override
    public PCBuildResponse savePCBuild(PCBuildRequest pcBuildRequest, String userId) {
        List<PCBuild> existingBuilds = pcBuilderRepository.getByUserID(userId);
        boolean buildWithSameNameExists = existingBuilds
                .stream()
                .anyMatch(build -> (!build.getId().equals(pcBuildRequest.getId())
                        && build.getName().equals(pcBuildRequest.getName())));

        if(buildWithSameNameExists)
            throw new RuntimeException("Build with same name already exists. Please choose a new name");

        PCBuild build = PCBuildMapper.dtoToEntity(pcBuildRequest, userId);
        build = pcBuilderRepository.save(build);
        return PCBuildMapper.entityToDto(build);
    }

    @Override
    public List<PCBuildResponse> getPCBuildsForUser(String userId) {
        List<PCBuild> builds = pcBuilderRepository.getByUserID(userId);

        return builds.stream()
                .map(PCBuildMapper::entityToDto)
                .toList();
    }

    @Override
    public void deletePCBuildById(String id) {
        pcBuilderRepository.deleteById(id);
    }
}
