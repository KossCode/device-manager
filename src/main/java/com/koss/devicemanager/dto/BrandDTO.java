package com.koss.devicemanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandDTO {
    private Long id;
    @NotBlank(message = "Brand is required")
    private String name;
}

