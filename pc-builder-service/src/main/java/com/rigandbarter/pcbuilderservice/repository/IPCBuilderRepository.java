package com.rigandbarter.pcbuilderservice.repository;

import com.rigandbarter.pcbuilderservice.model.PCBuild;

import java.util.List;

public interface IPCBuilderRepository {
    /**
     * Save the pc build in the db
     * @param entity The build to savce
     * @return The saved build
     */
    PCBuild save(PCBuild entity);

    /**
     * Gets the pc build associated with the user
     * @param userId The id of the user to get the build for
     * @return The pc build of user (if any)
     */
    List<PCBuild> getByUserID(String userId);

    /**
     * Deletes pc build by id
     * @param id The id of the pc build
     */
    void deleteById(String id);

    /**
     * Deletes all pc builds for a user
     * @param userId The id of the user to delete pc builds for
     */
    void deleteAllByUserId(String userId);
}
