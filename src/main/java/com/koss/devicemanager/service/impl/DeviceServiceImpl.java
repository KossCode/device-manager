package com.koss.devicemanager.service.impl;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.exception.DeviceNotFoundException;
import com.koss.devicemanager.mapper.DeviceMapper;
import com.koss.devicemanager.repository.DeviceRepository;
import com.koss.devicemanager.service.BrandService;
import com.koss.devicemanager.service.DeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final BrandService brandService;
    private final DeviceMapper deviceMapper;

    @Override
    public Page<DeviceDTO> getPaginatedDevices(Pageable pageable) {
        log.info("Retrieving paginated devices with page size: {}, page number: {}", pageable.getPageSize(), pageable.getPageNumber());
        var pageableDevices = deviceRepository.findAll(pageable);
        log.info("Total devices found: {}", pageableDevices.getTotalElements());
        return pageableDevices.map(deviceMapper::toDTO);
    }

    @Override
    public List<DeviceDTO> findAllDevices() {
        log.info("Retrieving all devices from the database.");
        var devices = deviceRepository.findAll().stream()
                .map(deviceMapper::toDTO)
                .toList();
        log.info("Number of devices retrieved: {}", devices.size());
        return devices;
    }

    @Override
    public DeviceDTO findDeviceById(Long id) {
        log.info("Attempting to find device by ID: {}", id);
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Device not found with ID: {}", id);
                    return new DeviceNotFoundException(id);
                });
        log.info("Device found: {}", device);
        return deviceMapper.toDTO(device);
    }

    @Override
    public List<DeviceDTO> findDevicesByBrand(String brand) {
        log.info("Finding devices for brand: {}", brand);
        var byBrandName = deviceRepository.findByBrandName(brand);
        log.info("Number of devices found for brand '{}': {}", brand, byBrandName.size());
        return byBrandName.stream().map(deviceMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public DeviceDTO addDevice(DeviceDTO deviceDTO) {
        log.info("Adding new device: {}", deviceDTO);
        var device = deviceMapper.toEntity(deviceDTO);
        device.setBrand(brandService.getOrCreateBrand(deviceDTO.getBrand()));
        var savedDevice = deviceMapper.toDTO(deviceRepository.save(device));
        log.info("Device added: {}", savedDevice);
        return savedDevice;
    }

    @Override
    @Transactional
    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO) {
        log.info("Updating device with ID: {}", id);
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Device not found with ID: {}", id);
                    return new DeviceNotFoundException(id);
                });

        existingDevice.setName(updatedDeviceDTO.getName());
        existingDevice.setBrand(brandService.getOrCreateBrand(updatedDeviceDTO.getBrand()));
        var updatedDevice = deviceMapper.toDTO(deviceRepository.save(existingDevice));
        log.info("Device updated: {}", updatedDevice);
        return updatedDevice;
    }

    @Override
    @Transactional
    public DeviceDTO patchDevice(Long id, DeviceDTO updatedDeviceDTO) {
        log.info("Partially updating device with ID: {}", id);
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Device not found with ID: {}", id);
                    return new DeviceNotFoundException(id);
                });

        if (updatedDeviceDTO.getName() != null) {
            existingDevice.setName(updatedDeviceDTO.getName());
            log.info("Device name updated to: {}", updatedDeviceDTO.getName());
        }

        if (updatedDeviceDTO.getBrand() != null) {
            existingDevice.setBrand(brandService.getOrCreateBrand(updatedDeviceDTO.getBrand()));
            log.info("Device brand updated to: {}", updatedDeviceDTO.getBrand());
        }

        var patchedDevice = deviceMapper.toDTO(deviceRepository.save(existingDevice));
        log.info("Device partially updated: {}", patchedDevice);
        return patchedDevice;
    }

    @Override
    public void deleteDevice(Long id) {
        log.info("Attempting to delete device with ID: {}", id);
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Device not found with ID: {}", id);
                    return new DeviceNotFoundException(id);
                });
        deviceRepository.delete(device);
        log.info("Device deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public List<DeviceDTO> saveAllDevices(List<DeviceDTO> devices) {
        log.info("Saving multiple devices, total count: {}", devices.size());
        var devicesToSave = devices.stream().map(d -> {
            var entity = deviceMapper.toEntity(d);
            entity.setBrand(brandService.getOrCreateBrand(d.getBrand()));
            return entity;
        }).toList();

        var savedDevices = deviceRepository.saveAll(devicesToSave)
                .stream()
                .map(deviceMapper::toDTO)
                .toList();
        log.info("Devices saved successfully, total count: {}", savedDevices.size());
        return savedDevices;
    }

    @Override
    public void deleteAllDevices() {
        log.info("Deleting all devices from the database.");
        deviceRepository.deleteAll();
        log.info("All devices deleted successfully.");
    }
}
