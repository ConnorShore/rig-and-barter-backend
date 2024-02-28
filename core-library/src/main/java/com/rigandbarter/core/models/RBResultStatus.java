package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RBResultStatus {
    public RBResultStatus(boolean success) {
        this.success = success;
        this.errorMessage = null;
    }

    private boolean success;
    private String errorMessage;
}
