package com.rigandbarter.componentservice.controller.impl;

import com.rigandbarter.componentservice.controller.IComponentController;
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
    public String healthCheck() {
        return "Component service is running...";
    }
}
