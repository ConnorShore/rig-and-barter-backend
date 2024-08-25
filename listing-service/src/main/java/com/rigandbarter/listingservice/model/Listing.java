package com.rigandbarter.listingservice.model;

import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.core.models.UserBasicInfo;
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
    private String stripeId;
    private List<String> imageUrls;
    private ComponentCategory componentCategory;
    private UserBasicInfo userInfo;

    // private ComponentCondition componentCondition;
    // private Set<String> tags;  ?? Not sure if needed
    // private ComponentType componentType;
    // private ComponentSpecifications componentSpecifications;

}
