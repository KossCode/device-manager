package com.koss.devicemanager.repository;

import com.koss.devicemanager.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}

