package com.koss.devicemanager.controller.user;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserDeviceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private UserDeviceController userDeviceController;

    private DeviceDTO deviceDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userDeviceController).build();

        deviceDTO = new DeviceDTO();
        deviceDTO.setId(1L);
        deviceDTO.setName("Device1");
        deviceDTO.setBrand("Brand1");
    }

    @Test
    void testGetDeviceByIdSuccess() throws Exception {
        when(deviceService.findDeviceById(1L)).thenReturn(deviceDTO);

        mockMvc.perform(get("/api/v1/user/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Device1")))
                .andExpect(jsonPath("$.message", is("Device retrieved successfully")));
    }

    @Test
    void testGetDevicesByBrandSuccess() throws Exception {
        when(deviceService.findDevicesByBrand("Brand1")).thenReturn(List.of(deviceDTO));

        mockMvc.perform(get("/api/v1/user/devices/brands/Brand1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].name", is("Device1")))
                .andExpect(jsonPath("$.message", is("Devices retrieved successfully")));
    }

    @Test
    void testListDevicesSuccess() throws Exception {
        PageRequest pageable = PageRequest.of(0, 50);
        List<DeviceDTO> devices = Collections.singletonList(deviceDTO);
        Page<DeviceDTO> pagedDevices = new PageImpl<>(devices, pageable, devices.size());

        when(deviceService.getPaginatedDevices(pageable)).thenReturn(pagedDevices);

        mockMvc.perform(get("/api/v1/user/devices")
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].name", is("Device1")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.message", is("Successfully fetched devices")));
    }

    @Test
    void testAddDeviceSuccess() throws Exception {
        when(deviceService.addDevice(Mockito.any(DeviceDTO.class))).thenReturn(deviceDTO);

        String jsonContent = "{\"name\": \"Device1\", \"brand\": \"Brand1\"}";

        mockMvc.perform(post("/api/v1/user/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Device1")))
                .andExpect(jsonPath("$.message", is("Successfully created device")));
    }

    @Test
    void testUpdateDeviceSuccess() throws Exception {
        when(deviceService.updateDevice(Mockito.anyLong(), Mockito.any(DeviceDTO.class))).thenReturn(deviceDTO);

        String jsonContent = "{\"name\": \"UpdatedDevice\", \"brand\": \"Brand1\"}";

        mockMvc.perform(put("/api/v1/user/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Device1")))  // Expecting the mocked deviceDTO data
                .andExpect(jsonPath("$.message", is("Successfully updated device entity")));
    }

    @Test
    void testPatchDeviceSuccess() throws Exception {
        when(deviceService.patchDevice(Mockito.anyLong(), Mockito.any(DeviceDTO.class))).thenReturn(deviceDTO);

        String jsonContent = "{\"name\": \"PatchedDevice\", \"brand\": \"Brand1\"}";

        mockMvc.perform(patch("/api/v1/user/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Device1")))  // Expecting the mocked deviceDTO data
                .andExpect(jsonPath("$.message", is("Successfully patched device entity")));
    }

    @Test
    void testDeleteDeviceSuccess() throws Exception {
        doNothing().when(deviceService).deleteDevice(Mockito.anyLong());

        mockMvc.perform(delete("/api/v1/user/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Successfully deleted device with id 1")));
    }
}
