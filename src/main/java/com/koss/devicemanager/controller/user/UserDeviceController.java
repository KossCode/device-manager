package com.koss.devicemanager.controller.user;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.dto.response.ExceptionResponseWrapper;
import com.koss.devicemanager.dto.response.ResponseWrapper;
import com.koss.devicemanager.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/devices")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDeviceController {
    private final DeviceService deviceService;

    private static final Integer MAX_ELEMENTS_PER_REQUEST = 50;

    @Operation(summary = "Retrieve a device by ID", description = "Fetches a specific device by its ID for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the device", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DeviceDTO>> getDeviceById(@PathVariable Long id) {
        var device = deviceService.findDeviceById(id);

        var response = new ResponseWrapper<>(device, "Device retrieved successfully", true);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve devices by brand name", description = "Fetches all devices by their brand name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the devices", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @GetMapping("/brands/{brand}")
    public ResponseEntity<ResponseWrapper<List<DeviceDTO>>> getDevicesByBrandName(@PathVariable String brand) {
        var devices = deviceService.findDevicesByBrand(brand);

        var response = new ResponseWrapper<>(devices, "Devices retrieved successfully", true);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get a list of devices for the user", description = "Returns a paginated list of devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of devices"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<DeviceDTO>>> listDevices(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "50") int limit) {

        var pageable = PageRequest.of(offset, Math.min(limit, MAX_ELEMENTS_PER_REQUEST));
        var pagedDevices = deviceService.getPaginatedDevices(pageable);

        var devices = pagedDevices.getContent();
        long totalElements = pagedDevices.getTotalElements();

        var response = new ResponseWrapper<>(
                devices,
                "Successfully fetched devices",
                true, totalElements);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a new device", description = "Creates a new device with validation checks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created successfully", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed with a map of all errors", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<DeviceDTO>> addDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        var createdDevice = deviceService.addDevice(deviceDTO);
        var response = new ResponseWrapper<>(createdDevice, "Successfully created device", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a device", description = "Updates an existing device entity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated device", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed with a map of all errors", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DeviceDTO>> updateDevice(
            @PathVariable Long id, @Valid @RequestBody DeviceDTO deviceDTO) {
        var updatedDevice = deviceService.updateDevice(id, deviceDTO);
        var response = new ResponseWrapper<>(updatedDevice, "Successfully updated device entity", true);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Patch a device", description = "Patches a device entity by ID, updating only provided fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched device", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed with a map of all errors", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DeviceDTO>> patchDevice(
            @PathVariable Long id, @RequestBody DeviceDTO deviceDTO) {
        var patchedDevice = deviceService.patchDevice(id, deviceDTO);
        var response = new ResponseWrapper<>(patchedDevice, "Successfully patched device entity", true);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a device", description = "Deletes a device by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted device", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> deleteDevice(@PathVariable Long id) {
        deviceService.findDeviceById(id);
        deviceService.deleteDevice(id);

        var response = new ResponseWrapper<>(null, "Successfully deleted device with id " + id, true);
        return ResponseEntity.ok(response);
    }
}
