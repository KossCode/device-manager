package com.koss.devicemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Data Transfer Object representing a device entity")
public class DeviceDTO {

    @Schema(description = "Unique identifier of the device", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the device", example = "Smartphone", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Device name is required")
    @Size(min = 2, max = 50, message = "Device name must be between 2 and 50 characters")
    private String name;

    @Schema(description = "Brand of the device", example = "Samsung", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Brand is required")
    private String brand;

    @Schema(description = "Timestamp of when the device was created", example = "2023-10-21T15:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime creationTime;
}
