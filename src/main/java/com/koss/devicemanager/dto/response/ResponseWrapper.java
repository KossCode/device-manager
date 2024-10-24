package com.koss.devicemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    private T data;
    private String message;
    private boolean success;
    private long totalElements;

    public ResponseWrapper(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    // Additional constructor for paginated data
    public ResponseWrapper(T data, String message, boolean success, long totalElements) {
        this.data = data;
        this.message = message;
        this.success = success;
        this.totalElements = totalElements;
    }
}

