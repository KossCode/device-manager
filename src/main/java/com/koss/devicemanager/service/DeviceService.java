package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.DeviceDTO;

import java.util.List;

public interface DeviceService {
    DeviceDTO findDeviceById(Long id);

    List<DeviceDTO> listDevices();

    DeviceDTO addDevice(DeviceDTO deviceDTO);

    DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO);

    void deleteDevice(Long id);

}
