package com.koss.devicemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeviceDTO {
    private Long id;
    @NotBlank(message = "Device name is required")
    @Size(min = 2, max = 50, message = "Device name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Brand is required")
    private String brand;
    private LocalDateTime creationTime;
}

