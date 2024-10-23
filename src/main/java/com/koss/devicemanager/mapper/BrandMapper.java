package com.koss.devicemanager.mapper;

import com.koss.devicemanager.dto.BrandDTO;
import com.koss.devicemanager.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandDTO toDTO(Brand brand);

    Brand toEntity(BrandDTO brandDTO);
}

