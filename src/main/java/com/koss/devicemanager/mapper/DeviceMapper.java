package com.koss.devicemanager.mapper;

import com.koss.devicemanager.dto.DeviceDTO;
import com.koss.devicemanager.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(source = "brand.name", target = "brand")
    DeviceDTO toDTO(Device device);

    @Mapping(source = "brand", target = "brand.name")
    Device toEntity(DeviceDTO deviceDTO);
}

