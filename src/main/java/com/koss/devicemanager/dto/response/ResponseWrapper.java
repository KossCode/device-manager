package com.koss.devicemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard wrapper for successful responses, supporting both single and paginated data")
public class ResponseWrapper<T> {

    @Schema(description = "Response data", example = "{ 'id': 1, 'name': 'Device' }", nullable = true)
    private T data;

    @Schema(description = "Message providing additional information about the response", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Indicates whether the operation was successful", example = "true")
    private boolean success;

    @Schema(description = "Total number of elements for paginated data", example = "100", nullable = true)
    private long totalElements;

    public ResponseWrapper(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ResponseWrapper(T data, String message, boolean success, long totalElements) {
        this.data = data;
        this.message = message;
        this.success = success;
        this.totalElements = totalElements;
    }
}
