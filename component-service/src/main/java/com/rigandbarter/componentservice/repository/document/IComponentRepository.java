package com.rigandbarter.componentservice.repository.document;

import com.rigandbarter.componentservice.model.Component;
import com.rigandbarter.core.models.ComponentCategory;

import java.util.List;

public interface IComponentRepository {

    /**
     * Saves all components to db
     * @param components Components to save
     * @return List of the saved components
     */
    List<Component> saveAllComponents(List<Component> components);

    /**
     * Gets all components in db
     * @return All components in db
     */
    List<Component> getAllComponents();

    /**
     * Gets all components of a specified cateogry
     * @param category Category of components to retrieve
     * @return All components of the specified category
     */
    List<Component> getAllComponentsOfCategory(ComponentCategory category);
}
