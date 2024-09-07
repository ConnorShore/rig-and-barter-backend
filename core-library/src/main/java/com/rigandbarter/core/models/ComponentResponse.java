package com.rigandbarter.core.models;

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
