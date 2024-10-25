package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.entity.Brand;
import com.koss.devicemanager.entity.Device;
import com.koss.devicemanager.exception.DeviceNotFoundException;
import com.koss.devicemanager.mapper.DeviceMapper;
import com.koss.devicemanager.repository.DeviceRepository;
import com.koss.devicemanager.service.impl.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private BrandService brandService;
    @Mock
    private DeviceMapper deviceMapper;
    @InjectMocks
    private DeviceServiceImpl deviceService;
    private Device device;
    private DeviceDTO deviceDTO;
    private Brand brand;

    @BeforeEach
    void setUp() {
        openMocks(this);

        brand = new Brand();
        brand.setId(1L);
        brand.setName("Test Brand");

        device = new Device();
        device.setId(1L);
        device.setName("Test Device");
        device.setBrand(brand);

        deviceDTO = new DeviceDTO();
        deviceDTO.setId(1L);
        deviceDTO.setName("Test Device");
        deviceDTO.setBrand("Test Brand");
    }

    @Test
    void getPaginatedDevices_ShouldReturnPaginatedDevices() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Device> devicePage = new PageImpl<>(List.of(device));

        when(deviceRepository.findAll(pageable)).thenReturn(devicePage);
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        Page<DeviceDTO> result = deviceService.getPaginatedDevices(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Device", result.getContent().get(0).getName());
        verify(deviceRepository).findAll(pageable);
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void findAllDevices_ShouldReturnAllDevices() {
        when(deviceRepository.findAll()).thenReturn(List.of(device));
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        List<DeviceDTO> result = deviceService.findAllDevices();

        assertEquals(1, result.size());
        assertEquals("Test Device", result.get(0).getName());
        verify(deviceRepository).findAll();
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void findDeviceById_ShouldReturnDevice() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.findDeviceById(1L);

        assertEquals("Test Device", result.getName());
        verify(deviceRepository).findById(1L);
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void findDevicesByBrand_ShouldReturnDevice() {
        when(deviceRepository.findByBrandName("Test Brand")).thenReturn(List.of(device));
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        List<DeviceDTO> result = deviceService.findDevicesByBrand("Test Brand");

        assertEquals("Test Device", result.get(0).getName());
        verify(deviceRepository).findByBrandName("Test Brand");
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void findDeviceById_ShouldThrowDeviceNotFoundException() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.findDeviceById(1L));
        verify(deviceRepository).findById(1L);
    }

    @Test
    void addDevice_ShouldSaveAndReturnDevice() {
        when(brandService.getOrCreateBrand("Test Brand")).thenReturn(brand);
        when(deviceMapper.toEntity(deviceDTO)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.addDevice(deviceDTO);

        assertEquals("Test Device", result.getName());
        verify(brandService).getOrCreateBrand("Test Brand");
        verify(deviceRepository).save(device);
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void updateDevice_ShouldUpdateAndReturnDevice() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(brandService.getOrCreateBrand("Test Brand")).thenReturn(brand);
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.updateDevice(1L, deviceDTO);

        assertEquals("Test Device", result.getName());
        verify(deviceRepository).findById(1L);
        verify(deviceRepository).save(device);
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void patchDevice_ShouldPartiallyUpdateAndReturnDevice() {
        DeviceDTO partialUpdateDTO = new DeviceDTO();
        partialUpdateDTO.setName("Updated Device");

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.patchDevice(1L, partialUpdateDTO);

        assertEquals("Test Device", result.getName());
        verify(deviceRepository).findById(1L);
        verify(deviceRepository).save(device);
        verify(deviceMapper).toDTO(device);
    }

    @Test
    void deleteDevice_ShouldDeleteDeviceById() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(1L);

        verify(deviceRepository).delete(device);
    }

    @Test
    void deleteAllDevices_ShouldDeleteAllDevices() {
        deviceService.deleteAllDevices();

        verify(deviceRepository).deleteAll();
    }

    @Test
    void saveAllDevices_ShouldSaveAndReturnDevices() {
        when(brandService.getOrCreateBrand("Test Brand")).thenReturn(brand);
        when(deviceMapper.toEntity(deviceDTO)).thenReturn(device);
        when(deviceRepository.saveAll(List.of(device))).thenReturn(List.of(device));
        when(deviceMapper.toDTO(device)).thenReturn(deviceDTO);

        List<DeviceDTO> result = deviceService.saveAllDevices(List.of(deviceDTO));

        assertEquals(1, result.size());
        assertEquals("Test Device", result.get(0).getName());
        verify(deviceRepository).saveAll(List.of(device));
        verify(deviceMapper).toDTO(device);
    }
}

