package com.rigandbarter.componentservice.dto;

import com.rigandbarter.core.models.ComponentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedComponentResponse {
    private int numItems;
    private List<ComponentResponse> components;
}
