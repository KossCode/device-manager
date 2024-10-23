package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.mapper.DeviceMapper;
import com.koss.devicemanager.repository.BrandRepository;
import com.koss.devicemanager.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final BrandRepository brandRepository;
    private final DeviceMapper deviceMapper;

    public List<DeviceDTO> listDevices() {
        return deviceRepository.findAll().stream()
                .map(deviceMapper::toDTO)
                .toList();
    }

    public DeviceDTO findDeviceById(Long id) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        return deviceMapper.toDTO(device);
    }

    public DeviceDTO addDevice(DeviceDTO deviceDTO) {
        var device = deviceMapper.toEntity(deviceDTO);
        var brand = brandRepository.findById(device.getBrand().getId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
        device.setBrand(brand);
        return deviceMapper.toDTO(deviceRepository.save(device));
    }

    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO) {
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        existingDevice.setName(updatedDeviceDTO.getName());
        var brand = updatedDeviceDTO.getBrand();
        existingDevice.setBrand(brandRepository.findByName(brand)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found")));
        return deviceMapper.toDTO(deviceRepository.save(existingDevice));
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}


