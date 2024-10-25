package com.koss.devicemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(implementation = ExceptionResponseWrapper.class, description = "Standard wrapper for error responses")
public class ExceptionResponseWrapper<T> {

    @Schema(description = "Additional error data, such as validation errors", example = "{ 'fieldName': 'error message' }", nullable = true)
    private T data;

    @Schema(description = "HTTP status code corresponding to the error", example = "400")
    private int code;

    @Schema(description = "Message providing information about the error", example = "Validation failed")
    private String message;

    @Schema(description = "Indicates whether the operation was successful", example = "false")
    private boolean success;

    public ExceptionResponseWrapper(T data, int code, String message, boolean success) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.success = success;
    }
}
