package com.koss.devicemanager.controller.user;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.dto.response.ExceptionResponseWrapper;
import com.koss.devicemanager.dto.response.ResponseWrapper;
import com.koss.devicemanager.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/brands")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserBrandController {
    private final BrandService brandService;

    @Operation(summary = "Get all brands", description = "Fetches all available brands")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of brands"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<BrandDTO>>> getAllBrands() {
        List<BrandDTO> brands = brandService.findAllBrands();
        var response = new ResponseWrapper<>(
                brands,
                "Successfully fetched brands",
                true);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a new brand", description = "Creates a new brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created brand", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<BrandDTO>> addBrand(@Valid @RequestBody BrandDTO brandDTO) {
        var createdBrand = brandService.addBrand(brandDTO);
        var response = new ResponseWrapper<>(
                createdBrand,
                "Successfully created brand",
                true);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a brand", description = "Updates an existing brand by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated brand", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BrandDTO>> updateBrand(
            @PathVariable Long id, @Valid @RequestBody BrandDTO brandDTO) {

        var updatedBrand = brandService.updateBrand(id, brandDTO);
        var response = new ResponseWrapper<>(
                updatedBrand,
                "Successfully updated brand",
                true);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a brand", description = "Deletes a brand by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted brand"),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> deleteBrand(@PathVariable Long id) {
        brandService.findById(id);
        brandService.deleteBrand(id);

        var response = new ResponseWrapper<>(
                null,
                "Successfully deleted brand with id " + id,
                true);

        return ResponseEntity.ok(response);
    }
}
