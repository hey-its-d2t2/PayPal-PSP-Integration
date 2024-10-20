package com.cpt.payments.constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PaymentMethodEnum {
    APM(1, "APM");

    private final int id;
    private final String name;

    PaymentMethodEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Method to get enum by id
    public static PaymentMethodEnum getById(int id) {
        for (PaymentMethodEnum method : values()) {
            if (method.getId() == id) {
                return method;
            }
        }
        
        log.error("No enum constant with id: " + id);
        return null;
    }

    // Method to get enum by name
    public static PaymentMethodEnum getByName(String name) {
        for (PaymentMethodEnum method : values()) {
            if (method.getName().equalsIgnoreCase(name)) {
                return method;
            }
        }
        
        log.error("No enum constant with name: " + name);
        return null;
    }
}

