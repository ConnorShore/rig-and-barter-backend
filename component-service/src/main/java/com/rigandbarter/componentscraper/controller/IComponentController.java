package com.rigandbarter.componentscraper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("api/component")
public interface IComponentController {



    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
