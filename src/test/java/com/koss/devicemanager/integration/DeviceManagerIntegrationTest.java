package com.koss.devicemanager.integration;

import com.koss.devicemanager.entity.Brand;
import com.koss.devicemanager.entity.Device;
import com.koss.devicemanager.repository.BrandRepository;
import com.koss.devicemanager.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class DeviceManagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();

        Brand brand1 = new Brand();
        brand1.setName("Brand1");

        Brand brand2 = new Brand();
        brand2.setName("Brand2");
        brandRepository.saveAll(List.of(brand1, brand2));

        Device device1 = new Device();
        device1.setName("Test Device 1");
        device1.setBrand(brand1);

        Device device2 = new Device();
        device2.setName("Test Device 2");
        device2.setBrand(brand2);
        deviceRepository.saveAll(List.of(device1, device2));
    }

    @Test
    void testGetDeviceById() throws Exception {
        Device device = deviceRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/user/devices/" + device.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Test Device 1")))
                .andExpect(jsonPath("$.data.brand", is("Brand1")));
    }

    @Test
    void testAddDevice() throws Exception {
        String newDeviceJson = "{\"name\": \"New Device\", \"brand\": \"BrandNew\"}";

        mockMvc.perform(post("/api/v1/user/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDeviceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("New Device")))
                .andExpect(jsonPath("$.data.brand", is("BrandNew")));

        var devices = deviceRepository.findAll();
        assert (devices.size() == 3);
    }

    @Test
    void testUpdateDevice() throws Exception {
        Device device = deviceRepository.findAll().get(0);

        String updatedDeviceJson = "{\"name\": \"Updated Device\", \"brand\": \"Updated Brand\"}";

        mockMvc.perform(put("/api/v1/user/devices/" + device.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedDeviceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Updated Device")))
                .andExpect(jsonPath("$.data.brand", is("Updated Brand")));

        Device updatedDevice = deviceRepository.findById(device.getId()).orElseThrow();
        assert (updatedDevice.getName().equals("Updated Device"));
        assert (updatedDevice.getBrand().getName().equals("Updated Brand"));
    }

    @Test
    void testDeleteDevice() throws Exception {
        Device device = deviceRepository.findAll().get(0);

        mockMvc.perform(delete("/api/v1/user/devices/" + device.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Successfully deleted device with id " + device.getId())));

        assert (deviceRepository.findById(device.getId()).isEmpty());
    }

    @Test
    void testListDevices() throws Exception {
        mockMvc.perform(get("/api/v1/user/devices")
                        .param("offset", "0")
                        .param("limit", "50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].name", is("Test Device 1")))
                .andExpect(jsonPath("$.data[1].name", is("Test Device 2")))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }
}
