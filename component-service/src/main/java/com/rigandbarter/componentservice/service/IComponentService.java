package com.rigandbarter.componentservice.service;

import com.rigandbarter.componentservice.dto.CreateComponentRequest;
import com.rigandbarter.componentservice.dto.PagedComponentResponse;
import com.rigandbarter.core.models.ComponentResponse;
import com.rigandbarter.core.models.ComponentCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IComponentService {

    /**
     * Creates a new component in the db
     * @param componentRequest The component to create
     * @param image The image to associate with the component
     * @return The created component
     */
    ComponentResponse createComponent(CreateComponentRequest componentRequest, MultipartFile image);

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


    /**
     * Gets components of specific category paginated and sorted
     * @param category the category of components to get
     * @param page The page index
     * @param numPerPage Number of entries per page
     * @param sortColumn Which column to sort on
     * @param descending True if values should be descending
     * @param searchTerm The text search string
     * @return The list of paged components with given params and the total num items
     */
    PagedComponentResponse getPaginatedComponentsOfCategory(ComponentCategory category,
                                                            int page,
                                                            int numPerPage,
                                                            String sortColumn,
                                                            boolean descending,
                                                            String searchTerm);
}
