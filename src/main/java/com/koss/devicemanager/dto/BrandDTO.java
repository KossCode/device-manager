package com.koss.devicemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data Transfer Object representing a brand entity")
public class BrandDTO {

    @Schema(description = "Unique identifier of the brand", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the brand", example = "Apple", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Brand is required")
    private String name;
}
