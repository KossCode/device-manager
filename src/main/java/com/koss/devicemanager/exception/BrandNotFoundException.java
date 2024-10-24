package com.koss.devicemanager.exception;

import jakarta.persistence.PersistenceException;

public class BrandNotFoundException extends PersistenceException {
    public BrandNotFoundException(Long id) {
        super(String.format("Brand with id %d not found", id));
    }
}
