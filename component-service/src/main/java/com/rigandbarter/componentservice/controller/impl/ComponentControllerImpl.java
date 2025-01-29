package com.rigandbarter.componentservice.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.componentservice.controller.IComponentController;
import com.rigandbarter.componentservice.dto.*;
import com.rigandbarter.componentservice.service.IComponentService;
import com.rigandbarter.core.models.ComponentResponse;
import com.rigandbarter.core.models.ComponentCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ComponentControllerImpl implements IComponentController {

    private final IComponentService componentService;

    @Override
    public ComponentResponse createComponent(String componentRequest, MultipartFile image) throws JsonProcessingException {
        int componentCategoryIndex = new ObjectMapper().readTree(componentRequest).get("componentCategory").asInt();
        CreateComponentRequest createComponentRequest = createComponentRequest(componentRequest, ComponentCategory.values()[componentCategoryIndex]);
        return componentService.createComponent(createComponentRequest, image);
    }

    @Override
    public List<ComponentResponse> updateComponentDb(MultipartFile dataZipFile) {
        return componentService.saveAllComponents(dataZipFile);
    }

    @Override
    public List<ComponentResponse> getAllComponents() {
        return componentService.getAllComponents();
    }

    @Override
    public List<ComponentResponse> getAllComponentsOfCategory(ComponentCategory category) {
        return componentService.getAllComponentsOfCategory(category);
    }

    @Override
    public PagedComponentResponse getPaginatedComponentsOfCategory(ComponentCategory category,
                                                                   int page,
                                                                   int numPerPage,
                                                                   String sortColumn,
                                                                   boolean descending,
                                                                   String searchTerm) {
        return componentService.getPaginatedComponentsOfCategory(category, page, numPerPage, sortColumn, descending, searchTerm);
    }

    @Override
    public String healthCheck() {
        return "Component service is running...";
    }

    private CreateComponentRequest createComponentRequest(String componentRequest, ComponentCategory category) throws JsonProcessingException {
        switch (category) {
            case CASE:
                return new ObjectMapper().readValue(componentRequest, CreateCaseComponentRequest.class);
            case CPU:
                return new ObjectMapper().readValue(componentRequest, CreateProcessorComponentRequest.class);
            case GPU:
                return new ObjectMapper().readValue(componentRequest, CreateVideoCardComponentRequest.class);
            case MEMORY:
                return new ObjectMapper().readValue(componentRequest, CreateMemoryComponentRequest.class);
            case MOTHERBOARD:
                return new ObjectMapper().readValue(componentRequest, CreateMotherboardComponentRequest.class);
            case POWER_SUPPLY:
                return new ObjectMapper().readValue(componentRequest, CreatePowerSupplyComponentRequest.class);
            case HARD_DRIVE:
                return new ObjectMapper().readValue(componentRequest, CreateHardDriveComponentRequest.class);
            case SOLID_STATE_DRIVE:
                return new ObjectMapper().readValue(componentRequest, CreateSolidStateDriveComponentRequest.class);
        }
        return null;
    }
}
