package com.koss.devicemanager.exception;

import jakarta.persistence.PersistenceException;

public class DeviceNotFoundException extends PersistenceException {
    public DeviceNotFoundException(Long id) {
        super(String.format("Device with id %d not found", id));
    }
}

