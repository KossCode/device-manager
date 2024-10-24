package com.koss.devicemanager.controller.admin;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.dto.response.ResponseWrapper;
import com.koss.devicemanager.service.DeviceService;
import com.koss.devicemanager.util.ValidList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AdminDeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<DeviceDTO>>> getAllDevices() {
        List<DeviceDTO> devices = deviceService.findAllDevices();

        var response = new ResponseWrapper<>(
                devices, "All devices retrieved", true, devices.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<ResponseWrapper<List<DeviceDTO>>> saveAllDevices(@RequestBody @Valid ValidList<DeviceDTO> devices) {
        List<DeviceDTO> savedDevices = deviceService.saveAllDevices(devices);

        var response = new ResponseWrapper<>(savedDevices, "All devices created", true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<ResponseWrapper<Object>> deleteAllDevices() {
        deviceService.deleteAllDevices();

        var response = new ResponseWrapper<>(
                null,
                "Successfully deleted all devices",
                true);

        return ResponseEntity.ok(response);
    }
}
