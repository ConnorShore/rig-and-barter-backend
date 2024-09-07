package com.rigandbarter.pcbuilderservice.controller;

import com.rigandbarter.pcbuilderservice.dto.PCBuildRequest;
import com.rigandbarter.pcbuilderservice.dto.PCBuildResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/pc-builder")
public interface IPCBuilderController {

    /**
     * Save the pc build for the user
     * @param pcBuildRequest The pc build to save
     * @param principal The user's auth principal
     * @return The cretaed PC Build
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    PCBuildResponse savePCBuild(@RequestBody PCBuildRequest pcBuildRequest, @AuthenticationPrincipal Jwt principal);

    /**
     * Gets the pc build for the user
     * @param principal The user's auth principal
     * @return The retrieved PC Build
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.CREATED)
    PCBuildResponse getPCBuildForUser(@AuthenticationPrincipal Jwt principal);

    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
