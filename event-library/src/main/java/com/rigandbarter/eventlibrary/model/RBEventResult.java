package com.rigandbarter.eventlibrary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RBEventResult {
    public RBEventResult(boolean success) {
        this.success = success;
        this.errorMessage = null;
    }

    private boolean success;
    private String errorMessage;
}
