package com.koss.devicemanager.service.impl;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.exception.DeviceNotFoundException;
import com.koss.devicemanager.mapper.DeviceMapper;
import com.koss.devicemanager.repository.DeviceRepository;
import com.koss.devicemanager.service.BrandService;
import com.koss.devicemanager.service.DeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final BrandService brandService;
    private final DeviceMapper deviceMapper;

    /**
     * Retrieves a paginated list of devices based on the provided pageable information.
     *
     * @param pageable the pagination information, including page size and sorting options
     * @return a page containing the found devices in the form of DeviceDTO
     */
    @Override
    public Page<DeviceDTO> getPaginatedDevices(Pageable pageable) {
        var pageableDevices = deviceRepository.findAll(pageable);
        return pageableDevices.map(deviceMapper::toDTO);
    }

    /**
     * Retrieves a list of all devices in the database.
     *
     * @return a list of DeviceDTO representing all devices
     */
    @Override
    public List<DeviceDTO> findAllDevices() {
        return deviceRepository.findAll().stream()
                .map(deviceMapper::toDTO)
                .toList();
    }

    /**
     * Finds a device by its unique ID.
     *
     * @param id the ID of the device to find
     * @return the found DeviceDTO or throws EntityNotFoundException if not found
     */
    @Override
    public DeviceDTO findDeviceById(Long id) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return deviceMapper.toDTO(device);
    }

    /**
     * Adds a new device to the database. If the associated brand does not exist,
     * it will be created automatically.
     *
     * @param deviceDTO the device data to add
     * @return the added DeviceDTO after being saved in the database
     */
    @Override
    @Transactional
    public DeviceDTO addDevice(DeviceDTO deviceDTO) {
        var device = deviceMapper.toEntity(deviceDTO);
        device.setBrand(brandService
                .getOrCreateBrand(deviceDTO.getBrand()));
        return deviceMapper.toDTO(deviceRepository.save(device));
    }

    /**
     * Full update of a device by replacing all fields.
     *
     * @param id               the ID of the device to update
     * @param updatedDeviceDTO the new data to update the device with
     * @return the updated DeviceDTO after being saved in the database
     */
    @Override
    @Transactional
    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO) {
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        existingDevice.setName(updatedDeviceDTO.getName());
        existingDevice.setBrand(brandService.getOrCreateBrand(updatedDeviceDTO.getBrand()));

        return deviceMapper.toDTO(deviceRepository.save(existingDevice));
    }

    /**
     * Partial update of a device by updating only non-null fields.
     *
     * @param id               the ID of the device to partially update
     * @param updatedDeviceDTO the partial data to update the device with
     * @return the updated DeviceDTO after being saved in the database
     */
    @Override
    @Transactional
    public DeviceDTO patchDevice(Long id, DeviceDTO updatedDeviceDTO) {
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        if (updatedDeviceDTO.getName() != null) {
            existingDevice.setName(updatedDeviceDTO.getName());
        }

        if (updatedDeviceDTO.getBrand() != null) {
            existingDevice.setBrand(brandService.getOrCreateBrand(updatedDeviceDTO.getBrand()));
        }

        return deviceMapper.toDTO(deviceRepository.save(existingDevice));
    }

    /**
     * Deletes a device from the database by its unique ID.
     *
     * @param id the ID of the device to delete
     */
    public void deleteDevice(Long id) {
        //todo: findBy before delete
        deviceRepository.deleteById(id);
    }

    /**
     * Saves a list of devices to the database. If a device's associated brand does not exist,
     * it will be created automatically.
     *
     * @param devices a list of DeviceDTOs to be saved
     * @return a list of saved DeviceDTOs after being stored in the database
     */
    @Override
    @Transactional
    public List<DeviceDTO> saveAllDevices(List<DeviceDTO> devices) {
        var devicesToSave = devices.stream().map(d -> {
            var entity = deviceMapper.toEntity(d);
            entity.setBrand(brandService.getOrCreateBrand(d.getBrand()));
            return entity;
        }).toList();

        return deviceRepository.saveAll(devicesToSave)
                .stream()
                .map(deviceMapper::toDTO)
                .toList();
    }

    /**
     * Deletes all devices from the database.
     */
    @Override
    public void deleteAllDevices() {
        deviceRepository.deleteAll();
    }
}


