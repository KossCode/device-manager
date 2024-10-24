package com.koss.devicemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponseWrapper<T> {
    private T data;
    private int code;
    private String message;
    private boolean success;

    public ExceptionResponseWrapper(T data, int code, String message, boolean success) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.success = success;
    }
}
