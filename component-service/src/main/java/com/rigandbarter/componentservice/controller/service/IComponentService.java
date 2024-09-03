package com.rigandbarter.componentservice.controller.service;

import com.rigandbarter.componentservice.dto.ComponentResponse;
import com.rigandbarter.core.models.ComponentCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IComponentService {

    /**
     * Saves components (that don't already exist) into the db
     * @param zipDataFile Zip file with all component csv files
     * @return All saved components
     */
    List<ComponentResponse> saveAllComponents(MultipartFile zipDataFile);

    /**
     * Gets all components in the db
     * @return All components in the db
     */
    List<ComponentResponse> getAllComponents();

    /**
     * Gets all components of a specified cateogry from the db
     * @param category The cateogry of components to retrieve
     * @return All components of the cateogry
     */
    List<ComponentResponse> getAllComponentsOfCategory(ComponentCategory category);
}
