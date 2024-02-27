package com.rigandbarter.eventlibrary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class RBEvent {
    private String id;
    private String userId;
    private String source;
    private LocalDateTime creationDate;
}
