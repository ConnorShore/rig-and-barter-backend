package com.rigandbarter.componentservice.dto;

import com.rigandbarter.core.models.ComponentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateComponentRequest {
    protected String name;
    protected String manufacturer;
    protected ComponentCategory componentCategory;
}
