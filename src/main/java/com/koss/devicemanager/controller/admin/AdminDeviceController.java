package com.koss.devicemanager.controller.admin;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.dto.response.ExceptionResponseWrapper;
import com.koss.devicemanager.dto.response.ResponseWrapper;
import com.koss.devicemanager.service.DeviceService;
import com.koss.devicemanager.util.ValidList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/admin/devices")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class AdminDeviceController {
    private final DeviceService deviceService;

    @Operation(summary = "Get all devices", description = "Fetches all available devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of devices"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<DeviceDTO>>> getAllDevices() {
        log.info("Fetching all devices.");
        List<DeviceDTO> devices = deviceService.findAllDevices();

        var response = new ResponseWrapper<>(
                devices, "All devices retrieved", true, devices.size());

        log.info("Successfully retrieved {} devices.", devices.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create multiple devices", description = "Saves a list of devices in bulk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created devices", content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @PostMapping("/bulk")
    public ResponseEntity<ResponseWrapper<List<DeviceDTO>>> saveAllDevices(@RequestBody @Valid ValidList<DeviceDTO> devices) {
        log.info("Saving {} devices.", devices.size());
        List<DeviceDTO> savedDevices = deviceService.saveAllDevices(devices);

        var response = new ResponseWrapper<>(savedDevices, "All devices created", true, savedDevices.size());

        log.info("Successfully created {} devices.", savedDevices.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete all devices", description = "Deletes all available devices in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted all devices"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionResponseWrapper.class)))
    })
    @DeleteMapping("/bulk")
    public ResponseEntity<ResponseWrapper<Object>> deleteAllDevices() {
        log.info("Deleting all devices.");
        deviceService.deleteAllDevices();

        var response = new ResponseWrapper<>(
                null,
                "Successfully deleted all devices",
                true);

        log.info("All devices successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
