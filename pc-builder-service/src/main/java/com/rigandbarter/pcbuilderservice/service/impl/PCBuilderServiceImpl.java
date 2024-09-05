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

@Service
@RequiredArgsConstructor
@Slf4j
public class PCBuilderServiceImpl implements IPCBuilderService {

    private final IPCBuilderRepository pcBuilderRepository;

    @Override
    public PCBuildResponse savePCBuild(PCBuildRequest pcBuildRequest, String userId) {
        PCBuild build = PCBuildMapper.dtoToEntity(pcBuildRequest, userId);
        build = pcBuilderRepository.save(build);
        return PCBuildMapper.entityToDto(build);
    }

    @Override
    public PCBuildResponse getPCBuildForUser(String userId) {
        PCBuild build = pcBuilderRepository.getByUserID(userId);
        if(build == null)
            return null;

        return PCBuildMapper.entityToDto(build);
    }
}
