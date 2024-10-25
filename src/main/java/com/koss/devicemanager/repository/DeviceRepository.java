package com.koss.devicemanager.repository;

import com.koss.devicemanager.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d from Device d where d.brand.name = ?1")
    List<Device> findByBrandName(String brandName);
}

