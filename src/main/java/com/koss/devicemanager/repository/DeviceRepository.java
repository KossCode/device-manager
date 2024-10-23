package com.koss.devicemanager.repository;

import com.koss.devicemanager.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByBrandName(String brandName);
}

