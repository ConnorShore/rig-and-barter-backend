package com.rigandbarter.listingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(value = "Listing")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private Double price;
    private String imageId;
//    private List<String> imageIds;
    private ComponentCategory componentCategory;

    // private Set<String> tags;  ?? Not sure if needed
    // private ComponentType componentType;
    // private ComponentSpecifications componentSpecifications;

}
