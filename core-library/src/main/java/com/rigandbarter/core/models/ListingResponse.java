package com.rigandbarter.core.models;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.ComponentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingResponse {
    private String id;
    private String userId;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private Double price;
    private List<String> imageUrls;
    private ComponentCategory componentCategory;
    private UserBasicInfoResponse userInfo;
    private boolean userVerified;
}
