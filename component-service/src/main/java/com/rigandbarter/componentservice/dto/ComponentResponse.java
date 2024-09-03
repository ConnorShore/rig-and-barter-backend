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
public class ComponentResponse {
    protected String id;
    protected String name;
    protected String manufacturer;
    protected String imageUrl;
    protected ComponentCategory category;
}
