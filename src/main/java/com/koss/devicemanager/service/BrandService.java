package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.BrandDTO;

import java.util.List;

public interface BrandService {

    BrandDTO findBrandById(Long id);

    List<BrandDTO> listBrands();

    BrandDTO addBrand(BrandDTO brandDTO);

    void deleteBrand(Long id);
}
