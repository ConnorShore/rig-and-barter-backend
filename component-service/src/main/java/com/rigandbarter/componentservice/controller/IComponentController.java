package com.rigandbarter.componentservice.controller;

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
     * Updates the component database with new entries
     * @return List of created components
     */
    @GetMapping("{category}")
    @ResponseStatus(HttpStatus.OK)
    List<ComponentResponse> getAllComponentsOfCategory(@PathVariable ComponentCategory category);

    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
