package com.koss.devicemanager.service;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.entity.Brand;
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

    private static final String BRAND_NOT_FOUND_MESSAGE = "Brand with id %s not found";

    /**
     * Finds a brand by its unique ID.
     *
     * @param id the ID of the brand to find
     * @return the found BrandDTO or throws EntityNotFoundException if not found
     */

    @Override
    public BrandDTO findById(Long id) {
        var brand = brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(BRAND_NOT_FOUND_MESSAGE, id)));
        return brandMapper.toDTO(brand);
    }

    /**
     * Retrieves a list of all brands in the database.
     *
     * @return a list of BrandDTO representing all brands
     */
    @Override
    public List<BrandDTO> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDTO)
                .toList();
    }

    /**
     * Adds a new brand to the database.
     *
     * @param brandDTO the brand data to add
     * @return the added BrandDTO after being saved in the database
     */
    @Override
    public BrandDTO addBrand(BrandDTO brandDTO) {
        var brand = brandMapper.toEntity(brandDTO);
        return brandMapper.toDTO(brandRepository.save(brand));
    }

    /**
     * Method to get an existing brand by name or create a new one if it doesn't exist.
     *
     * @param name the brand name to find or create
     * @return the found or newly created brand
     */
    @Override
    public Brand getOrCreateBrand(String name) {
        var existingBrand = brandRepository.findByName(name);

        if (existingBrand.isPresent()) {
            return existingBrand.get();
        }

        var newBrand = new Brand();
        newBrand.setName(name);

        return brandRepository.save(newBrand);
    }

    /**
     * Updates an existing brand's information with new data.
     *
     * @param id              the ID of the brand to update
     * @param updatedBrandDTO the new data to update the brand with
     * @return the updated BrandDTO after being saved in the database
     */
    @Override
    public BrandDTO updateBrand(Long id, BrandDTO updatedBrandDTO) {
        var existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(BRAND_NOT_FOUND_MESSAGE, id)));
        existingBrand.setName(updatedBrandDTO.getName());
        return brandMapper.toDTO(brandRepository.save(existingBrand));

    }

    /**
     * Deletes a brand from the database by its unique ID.
     *
     * @param id the ID of the brand to delete
     */
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}

