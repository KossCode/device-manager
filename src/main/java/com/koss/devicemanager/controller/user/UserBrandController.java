package com.koss.devicemanager.controller.user;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.dto.response.ResponseWrapper;
import com.koss.devicemanager.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/brands")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserBrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<BrandDTO>>> getAllBrands() {
        List<BrandDTO> brands = brandService.findAllBrands();
        var response = new ResponseWrapper<>(
                brands,
                "Successfully fetched brands",
                true);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<BrandDTO>> addBrand(@Valid @RequestBody BrandDTO brandDTO) {
        var createdBrand = brandService.addBrand(brandDTO);
        var response = new ResponseWrapper<>(
                createdBrand,
                "Successfully created brand",
                true);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BrandDTO>> updateBrand(
            @PathVariable Long id, @Valid @RequestBody BrandDTO brandDTO) {

        var updatedBrand = brandService.updateBrand(id, brandDTO);
        var response = new ResponseWrapper<>(
                updatedBrand,
                "Successfully updated brand",
                true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> deleteBrand(@PathVariable Long id) {
        brandService.findById(id);
        brandService.deleteBrand(id);

        var response = new ResponseWrapper<>(
                null,
                "Successfully deleted brand with id" + id,
                true);

        return ResponseEntity.ok(response);
    }
}
