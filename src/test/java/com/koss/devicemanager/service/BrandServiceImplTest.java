package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.entity.Brand;
import com.koss.devicemanager.exception.BrandNotFoundException;
import com.koss.devicemanager.mapper.BrandMapper;
import com.koss.devicemanager.repository.BrandRepository;
import com.koss.devicemanager.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandServiceImpl;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testFindById_Success() {
        Long brandId = 1L;
        Brand brand = new Brand();
        brand.setId(brandId);

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brandId);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        when(brandMapper.toDTO(brand)).thenReturn(brandDTO);

        BrandDTO result = brandServiceImpl.findById(brandId);

        assertEquals(brandId, result.getId());
        verify(brandRepository).findById(brandId);
        verify(brandMapper).toDTO(brand);
    }

    @Test
    void testFindById_NotFound() {
        Long brandId = 1L;

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandServiceImpl.findById(brandId));
        verify(brandRepository).findById(brandId);
    }

    @Test
    void testFindAllBrands_Success() {
        List<Brand> brands = List.of(new Brand(), new Brand());
        List<BrandDTO> brandDTOs = List.of(new BrandDTO(), new BrandDTO());

        when(brandRepository.findAll()).thenReturn(brands);
        when(brandMapper.toDTO(any(Brand.class))).thenReturn(brandDTOs.get(0), brandDTOs.get(1));

        List<BrandDTO> result = brandServiceImpl.findAllBrands();

        assertEquals(brandDTOs.size(), result.size());
        verify(brandRepository).findAll();
        verify(brandMapper, times(brands.size())).toDTO(any(Brand.class));
    }

    @Test
    void testAddBrand_Success() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Test Brand");

        Brand brand = new Brand();
        brand.setName("Test Brand");

        when(brandMapper.toEntity(brandDTO)).thenReturn(brand);
        when(brandRepository.save(brand)).thenReturn(brand);
        when(brandMapper.toDTO(brand)).thenReturn(brandDTO);

        BrandDTO result = brandServiceImpl.addBrand(brandDTO);

        assertEquals(brandDTO.getName(), result.getName());
        verify(brandMapper).toEntity(brandDTO);
        verify(brandRepository).save(brand);
        verify(brandMapper).toDTO(brand);
    }

    @Test
    void testGetOrCreateBrand_BrandExists() {
        String brandName = "Existing Brand";
        Brand existingBrand = new Brand();
        existingBrand.setName(brandName);

        when(brandRepository.findByName(brandName)).thenReturn(Optional.of(existingBrand));

        Brand result = brandServiceImpl.getOrCreateBrand(brandName);

        assertEquals(brandName, result.getName());
        verify(brandRepository).findByName(brandName);
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void testGetOrCreateBrand_BrandDoesNotExist() {
        String brandName = "New Brand";
        Brand newBrand = new Brand();
        newBrand.setName(brandName);

        when(brandRepository.findByName(brandName)).thenReturn(Optional.empty());
        when(brandRepository.save(any(Brand.class))).thenReturn(newBrand);

        Brand result = brandServiceImpl.getOrCreateBrand(brandName);

        assertEquals(brandName, result.getName());
        verify(brandRepository).findByName(brandName);
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void testUpdateBrand_Success() {
        Long brandId = 1L;
        BrandDTO updatedBrandDTO = new BrandDTO();
        updatedBrandDTO.setName("Updated Brand");

        Brand existingBrand = new Brand();
        existingBrand.setId(brandId);
        existingBrand.setName("Old Brand");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(existingBrand)).thenReturn(existingBrand);
        when(brandMapper.toDTO(existingBrand)).thenReturn(updatedBrandDTO);

        BrandDTO result = brandServiceImpl.updateBrand(brandId, updatedBrandDTO);

        assertEquals(updatedBrandDTO.getName(), result.getName());
        verify(brandRepository).findById(brandId);
        verify(brandRepository).save(existingBrand);
        verify(brandMapper).toDTO(existingBrand);
    }

    @Test
    void testUpdateBrand_NotFound() {
        Long brandId = 1L;
        BrandDTO updatedBrandDTO = new BrandDTO();
        updatedBrandDTO.setName("Updated Brand");

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandServiceImpl.updateBrand(brandId, updatedBrandDTO));
        verify(brandRepository).findById(brandId);
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void testDeleteBrand_Success() {
        Long brandId = 1L;
        Brand existingBrand = new Brand();
        existingBrand.setId(brandId);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));

        brandServiceImpl.deleteBrand(brandId);

        verify(brandRepository).findById(brandId);
        verify(brandRepository).delete(existingBrand);
    }

    @Test
    void testDeleteBrand_NotFound() {
        Long brandId = 1L;

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandServiceImpl.deleteBrand(brandId));
        verify(brandRepository).findById(brandId);
        verify(brandRepository, never()).delete(any(Brand.class));
    }
}

