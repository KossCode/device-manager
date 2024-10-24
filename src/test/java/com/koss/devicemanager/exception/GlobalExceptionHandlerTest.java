package com.koss.devicemanager.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koss.devicemanager.controller.user.UserDeviceController;
import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.service.DeviceService;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {GlobalExceptionHandler.class, UserDeviceController.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Test
    void testHandlePersistenceException() throws Exception {
        long deviceId = 1L;
        when(deviceService.findDeviceById(deviceId))
                .thenThrow(new PersistenceException("Entity not found"));

        mockMvc.perform(get("/api/v1/user/devices/{id}", deviceId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Entity not found"));
    }

    @Test
    void testHandleValidationException() throws Exception {
        DeviceDTO invalidDeviceDTO = new DeviceDTO();
        invalidDeviceDTO.setName("");
        invalidDeviceDTO.setBrand("");

        String deviceJson = new ObjectMapper().writeValueAsString(invalidDeviceDTO);

        mockMvc.perform(post("/api/v1/user/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.name").value(anyOf(
                        is("Device name must be between 2 and 50 characters"),
                        is("Device name is required")
                )))
                .andExpect(jsonPath("$.data.brand").value("Brand is required"));
    }

    @Test
    void testHandleRuntimeException() throws Exception {
        long deviceId = 1L;
        when(deviceService.findDeviceById(deviceId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/user/devices/{id}", deviceId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}
