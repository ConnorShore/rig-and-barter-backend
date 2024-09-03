package com.rigandbarter.componentservice.controller;

import com.rigandbarter.componentservice.dto.ComponentResponse;
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
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
