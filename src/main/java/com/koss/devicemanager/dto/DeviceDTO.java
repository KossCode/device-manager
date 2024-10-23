package com.koss.devicemanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeviceDTO {
    private Long id;
    private String name;
    private String brand;
    private LocalDateTime creationTime;
}

