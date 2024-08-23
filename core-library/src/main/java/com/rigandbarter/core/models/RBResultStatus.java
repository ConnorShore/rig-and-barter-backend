package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RBResultStatus<T> {

    protected boolean success;
    protected String errorMessage;
    protected T value;

    public RBResultStatus(boolean success) {
        this.success = success;
        this.errorMessage = null;
        this.value = null;
    }

    public RBResultStatus(boolean success, T value) {
        this.success = success;
        this.errorMessage = null;
        this.value = value;
    }

    public RBResultStatus(boolean success, String message) {
        this.success = success;
        this.errorMessage = message;
        this.value = null;
    }
}