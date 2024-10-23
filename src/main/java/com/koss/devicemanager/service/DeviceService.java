package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.DeviceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceService {
    DeviceDTO findDeviceById(Long id);

    List<DeviceDTO> findAllDevices();

    DeviceDTO addDevice(DeviceDTO deviceDTO);

    DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO);

    DeviceDTO patchDevice(Long id, DeviceDTO updatedDeviceDTO);

    void deleteDevice(Long id);

    List<DeviceDTO> saveAllDevices(List<DeviceDTO> devices);

    void deleteAllDevices();

    Page<DeviceDTO> getPaginatedDevices(Pageable pageable);
}
