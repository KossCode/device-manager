package com.koss.devicemanager.repository;

import com.koss.devicemanager.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}

