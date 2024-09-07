package com.rigandbarter.componentservice.controller;

import com.rigandbarter.componentservice.dto.PagedComponentResponse;
import com.rigandbarter.core.models.ComponentResponse;
import com.rigandbarter.core.models.ComponentCategory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("api/component")
public interface IComponentController {

    /**
     * Updates the component database with new entries
     * @param dataZipFile The zip containing data entries
     * @return List of created components
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    List<ComponentResponse> updateComponentDb(@RequestPart(name = "dataZip") MultipartFile dataZipFile);

    /**
     * Updates the component database with new entries
     * @return List of created components
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<ComponentResponse> getAllComponents();

    /**
     * Gets all components of specific category
     * @return List of components of that category
     */
    @GetMapping("{category}")
    @ResponseStatus(HttpStatus.OK)
    List<ComponentResponse> getAllComponentsOfCategory(@PathVariable ComponentCategory category);

    /**
     * Gets all components of category paged
     * @return Paged components and the total number of items
     */
    @GetMapping("{category}/paged")
    @ResponseStatus(HttpStatus.OK)
    PagedComponentResponse getPaginatedComponentsOfCategory(@PathVariable ComponentCategory category,
                                                            @RequestParam int page,
                                                            @RequestParam int numPerPage,
                                                            @RequestParam String sortColumn,
                                                            @RequestParam boolean descending,
                                                            @RequestParam String searchTerm);

    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
