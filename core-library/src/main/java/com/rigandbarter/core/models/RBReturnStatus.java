package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RBReturnStatus {
    private boolean success;
    private String errorMessage;

    public RBReturnStatus(boolean success) {
        this.success = success;
        this.errorMessage = "";
    }
}
