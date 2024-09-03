package com.rigandbarter.componentscraper.controller.impl;

import com.rigandbarter.componentscraper.controller.IComponentController;
import com.rigandbarter.componentscraper.controller.service.IComponentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ComponentController implements IComponentController {

    private final IComponentService componentService;

    @Override
    public String healthCheck() {
        return "Component service is running...";
    }
}
