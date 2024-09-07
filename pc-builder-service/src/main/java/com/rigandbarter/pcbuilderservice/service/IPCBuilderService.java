package com.rigandbarter.pcbuilderservice.service;

import com.rigandbarter.pcbuilderservice.dto.PCBuildRequest;
import com.rigandbarter.pcbuilderservice.dto.PCBuildResponse;

import java.util.List;

public interface IPCBuilderService {

    /**
     * Save the pc build for the given user
     * @param pcBuildRequest The pc build to save
     * @param userId The id of the user to save the build for
     * @return The created pc build
     */
    PCBuildResponse savePCBuild(PCBuildRequest pcBuildRequest, String userId);


    /**
     * Gets the pc build for the given user
     * @param userId The id of the user to get the build for
     * @return The retrieved pc build
     */
    PCBuildResponse getPCBuildForUser(String userId);
}
