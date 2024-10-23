package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.entity.Brand;

import java.util.List;

public interface BrandService {
    List<BrandDTO> findAllBrands();
    BrandDTO addBrand(BrandDTO brandDTO);
    Brand getOrCreateBrand(String name);
    BrandDTO updateBrand(Long id, BrandDTO updatedBrandDTO);
    void deleteBrand(Long id);

    BrandDTO findById(Long id);
}
