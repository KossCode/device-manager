package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.mapper.BrandMapper;
import com.koss.devicemanager.repository.BrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandDTO findBrandById(Long id) {
        var brand = brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
        return brandMapper.toDTO(brand);
    }

    public List<BrandDTO> listBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDTO)
                .toList();
    }

    public BrandDTO addBrand(BrandDTO brandDTO) {
        var brand = brandMapper.toEntity(brandDTO);
        return brandMapper.toDTO(brandRepository.save(brand));
    }

    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}

