package com.rigandbarter.componentservice.model;

import com.rigandbarter.core.models.ComponentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Document(value = "Component")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Component {
    protected String id;

    @TextIndexed(weight = 2) protected String name;
    @TextIndexed() protected String manufacturer;

    protected String imageUrl;
    protected ComponentCategory category;

    @TextScore() protected Float score;
}
