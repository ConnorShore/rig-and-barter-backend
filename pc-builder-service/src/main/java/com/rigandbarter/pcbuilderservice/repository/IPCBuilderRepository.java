package com.rigandbarter.pcbuilderservice.repository;

import com.rigandbarter.pcbuilderservice.model.PCBuild;

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
    PCBuild getByUserID(String userId);
}
