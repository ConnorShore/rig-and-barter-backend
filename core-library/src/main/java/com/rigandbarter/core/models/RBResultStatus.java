package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RBResultStatus {

    protected boolean success;
    protected String errorMessage;

    public RBResultStatus(boolean success) {
        this.success = success;
        this.errorMessage = null;
    }
}
