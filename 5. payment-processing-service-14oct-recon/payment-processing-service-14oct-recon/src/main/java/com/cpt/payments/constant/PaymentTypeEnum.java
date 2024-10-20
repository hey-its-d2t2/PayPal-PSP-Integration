package com.cpt.payments.constant;

public enum PaymentTypeEnum {
    SALE(1, "SALE");

    private final int id;
    private final String name;

    PaymentTypeEnum(int id, String name) {
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
    public static PaymentTypeEnum getById(int id) {
        for (PaymentTypeEnum type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    // Method to get enum by name
    public static PaymentTypeEnum getByName(String name) {
        for (PaymentTypeEnum type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
