package com.rigandbarter.messageservice.componentdbservice.model;

import com.rigandbarter.core.models.ComponentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Component {
    private String name;
    private String manufacturer;
    private String imageUrl;
    private ComponentCategory category;
}
