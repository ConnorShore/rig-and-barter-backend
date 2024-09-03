package com.rigandbarter.componentservice.model;

import com.rigandbarter.core.models.ComponentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "Component")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Component {
    protected String id;
    protected String name;
    protected String manufacturer;
    protected String imageUrl;
    protected ComponentCategory category;
}
