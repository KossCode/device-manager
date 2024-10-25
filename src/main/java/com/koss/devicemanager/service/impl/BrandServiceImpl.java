package com.koss.devicemanager.service.impl;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.entity.Brand;
import com.koss.devicemanager.exception.BrandNotFoundException;
import com.koss.devicemanager.mapper.BrandMapper;
import com.koss.devicemanager.repository.BrandRepository;
import com.koss.devicemanager.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandDTO findById(Long id) {
        log.info("Attempting to find brand by ID: {}", id);
        var brand = brandRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Brand not found with ID: {}", id);
                    return new BrandNotFoundException(id);
                });
        log.info("Brand found: {}", brand);
        return brandMapper.toDTO(brand);
    }

    @Override
    public List<BrandDTO> findAllBrands() {
        log.info("Retrieving all brands from the database.");
        var brands = brandRepository.findAll().stream()
                .map(brandMapper::toDTO)
                .toList();
        log.info("Number of brands retrieved: {}", brands.size());
        return brands;
    }

    @Override
    public BrandDTO addBrand(BrandDTO brandDTO) {
        log.info("Adding new brand: {}", brandDTO);
        var brand = brandMapper.toEntity(brandDTO);
        var savedBrand = brandMapper.toDTO(brandRepository.save(brand));
        log.info("Brand added: {}", savedBrand);
        return savedBrand;
    }

    @Override
    public Brand getOrCreateBrand(String name) {
        log.info("Getting or creating brand with name: {}", name);
        var existingBrand = brandRepository.findByName(name);

        if (existingBrand.isPresent()) {
            log.info("Brand already exists: {}", existingBrand.get());
            return existingBrand.get();
        }

        var newBrand = new Brand();
        newBrand.setName(name);
        var savedBrand = brandRepository.save(newBrand);
        log.info("New brand created: {}", savedBrand);
        return savedBrand;
    }

    @Override
    public BrandDTO updateBrand(Long id, BrandDTO updatedBrandDTO) {
        log.info("Updating brand with ID: {}", id);
        var existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Brand not found with ID: {}", id);
                    return new BrandNotFoundException(id);
                });
        existingBrand.setName(updatedBrandDTO.getName());
        var updatedBrand = brandMapper.toDTO(brandRepository.save(existingBrand));
        log.info("Brand updated: {}", updatedBrand);
        return updatedBrand;
    }

    @Override
    public void deleteBrand(Long id) {
        log.info("Attempting to delete brand with ID: {}", id);
        var brand = brandRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Brand not found with ID: {}", id);
                    return new BrandNotFoundException(id);
                });
        brandRepository.delete(brand);
        log.info("Brand deleted with ID: {}", id);
    }
}
