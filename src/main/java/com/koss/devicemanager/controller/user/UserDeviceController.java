package com.koss.devicemanager.controller.user;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.dto.response.ResponseWrapper;
import com.koss.devicemanager.service.DeviceService;
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
    //todo: prevent XSS vulnerabilities
    //todo: swagger api
    private final DeviceService deviceService;

    private static final Integer MAX_ELEMENTS_PER_REQUEST = 50;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DeviceDTO>> getDeviceById(@PathVariable Long id) {
        var device = deviceService.findDeviceById(id);

        var response = new ResponseWrapper<>(
                device, "Device retrieved successfully", true);

        return ResponseEntity.ok(response);
    }

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

    @PostMapping
    public ResponseEntity<ResponseWrapper<DeviceDTO>> addDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        var createdDevice = deviceService.addDevice(deviceDTO);
        var response = new ResponseWrapper<>(
                createdDevice,
                "Successfully create device",
                true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DeviceDTO>> updateDevice(
            @PathVariable Long id, @Valid @RequestBody DeviceDTO deviceDTO) {

        var updatedDevice = deviceService.updateDevice(id, deviceDTO);
        var response = new ResponseWrapper<>(
                updatedDevice,
                "Successfully updated device entity",
                true);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DeviceDTO>> patchDevice(
            @PathVariable Long id, @RequestBody DeviceDTO deviceDTO) {

        var patchedDevice = deviceService.patchDevice(id, deviceDTO);
        var response = new ResponseWrapper<>(
                patchedDevice,
                "Successfully patched device entity",
                true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> deleteDevice(@PathVariable Long id) {
        deviceService.findDeviceById(id);
        deviceService.deleteDevice(id);

        var response = new ResponseWrapper<>(
                null,
                "Successfully deleted device with id" + id,
                true);

        return ResponseEntity.ok(response);
    }
}

