package com.rigandbarter.pcbuilderservice.controller.impl;

import com.rigandbarter.pcbuilderservice.controller.IPCBuilderController;
import com.rigandbarter.pcbuilderservice.dto.PCBuildRequest;
import com.rigandbarter.pcbuilderservice.dto.PCBuildResponse;
import com.rigandbarter.pcbuilderservice.service.IPCBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PCBuilderControllerImpl implements IPCBuilderController {

    private final IPCBuilderService pcBuilderService;

    @Override
    public PCBuildResponse savePCBuild(PCBuildRequest pcBuildRequest, Jwt principal) {
        return pcBuilderService.savePCBuild(pcBuildRequest, principal.getSubject());
    }

    @Override
    public List<PCBuildResponse> getPCBuildsForUser(Jwt principal) {
        return pcBuilderService.getPCBuildsForUser(principal.getSubject());
    }

    @Override
    public void deletePCBuildById(String buildId) {
        pcBuilderService.deletePCBuildById(buildId);
    }

    @Override
    public String healthCheck() {
        return "PC Builder Service is running";
    }
}
