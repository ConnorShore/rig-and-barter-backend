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

    /**
     * Gets components of specific category paginated and sorted
     * @param category the category of components to get
     * @param page The page index
     * @param numPerPage Number of entries per page
     * @param sortColumn Which column to sort on
     * @param descending True if values should be descending
     * @param searchTerm The text to include for matching results
     * @return The list of components with given params
     */
    List<Component> getPaginatedComponentsOfCategory(ComponentCategory category,
                                                     int page,
                                                     int numPerPage,
                                                     String sortColumn,
                                                     boolean descending,
                                                     String searchTerm);

    /**
     * Gets number of components of specific category and search
     * @param category the category of components to get
     * @param searchTerm The text to include for matching results
     * @return The number of components with given params
     */
    int getPaginatedComponentsOfCategorySize(ComponentCategory category, String searchTerm);
}
