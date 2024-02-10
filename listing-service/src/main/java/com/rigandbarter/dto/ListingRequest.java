package com.rigandbarter.dto;

import com.rigandbarter.model.ComponentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingRequest {
    private String title;
    private String description;
    private Double price;
    private ComponentCategory componentCategory;
}
