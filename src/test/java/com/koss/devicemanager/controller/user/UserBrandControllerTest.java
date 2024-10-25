package com.koss.devicemanager.controller.user;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserBrandControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private UserBrandController userBrandController;

    private BrandDTO brandDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userBrandController).build();

        brandDTO = new BrandDTO();
        brandDTO.setId(1L);
        brandDTO.setName("Brand1");
    }

    @Test
    void testGetAllBrandsSuccess() throws Exception {
        when(brandService.findAllBrands()).thenReturn(Collections.singletonList(brandDTO));

        mockMvc.perform(get("/api/v1/user/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].name", is("Brand1")))
                .andExpect(jsonPath("$.message", is("Successfully fetched brands")));
    }

    @Test
    void testAddBrandSuccess() throws Exception {
        when(brandService.addBrand(Mockito.any(BrandDTO.class))).thenReturn(brandDTO);

        String jsonContent = "{\"name\": \"Brand1\"}";

        mockMvc.perform(post("/api/v1/user/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Brand1")))
                .andExpect(jsonPath("$.message", is("Successfully created brand")));
    }

    @Test
    void testUpdateBrandSuccess() throws Exception {
        when(brandService.updateBrand(Mockito.anyLong(), Mockito.any(BrandDTO.class))).thenReturn(brandDTO);

        String jsonContent = "{\"name\": \"UpdatedBrand\"}";

        mockMvc.perform(put("/api/v1/user/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Brand1")))  // Expecting the mocked brandDTO data
                .andExpect(jsonPath("$.message", is("Successfully updated brand")));
    }

    @Test
    void testDeleteBrandSuccess() throws Exception {
        doNothing().when(brandService).deleteBrand(Mockito.anyLong());

        mockMvc.perform(delete("/api/v1/user/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Successfully deleted brand with id 1")));
    }
}

