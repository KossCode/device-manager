package com.koss.devicemanager.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminDeviceController.class)
class AdminDeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Autowired
    private ObjectMapper objectMapper;

    private DeviceDTO deviceDTO;

    @BeforeEach
    void setUp() {
        deviceDTO = new DeviceDTO();
        deviceDTO.setName("Device1");
        deviceDTO.setBrand("Brand1");
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void testGetAllDevicesSuccess() throws Exception {
        List<DeviceDTO> devices = Collections.singletonList(deviceDTO);
        Mockito.when(deviceService.findAllDevices()).thenReturn(devices);

        mockMvc.perform(get("/api/v1/admin/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].name", is("Device1")))
                .andExpect(jsonPath("$.data[0].brand", is("Brand1")))
                .andExpect(jsonPath("$.message", is("All devices retrieved")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void testSaveAllDevicesSuccess() throws Exception {
        List<DeviceDTO> devices = Collections.singletonList(deviceDTO);
        Mockito.when(deviceService.saveAllDevices(Mockito.anyList())).thenReturn(devices);

        mockMvc.perform(post("/api/v1/admin/devices/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(devices)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].name", is("Device1")))
                .andExpect(jsonPath("$.data[0].brand", is("Brand1")))
                .andExpect(jsonPath("$.message", is("All devices created")));
    }

    @Test
    void testDeleteAllDevicesSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/devices/bulk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Successfully deleted all devices")));

        Mockito.verify(deviceService).deleteAllDevices();
    }
}
