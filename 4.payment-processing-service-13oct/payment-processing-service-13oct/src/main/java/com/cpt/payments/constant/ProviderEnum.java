package com.cpt.payments.constant;

public enum ProviderEnum {
    PAYPAL(1, "PAYPAL");

    private final int id;
    private final String name;

    ProviderEnum(int id, String name) {
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
    public static ProviderEnum getById(int id) {
        for (ProviderEnum provider : values()) {
            if (provider.getId() == id) {
                return provider;
            }
        }
        return null;
    }

    // Method to get enum by name
    public static ProviderEnum getByName(String name) {
        for (ProviderEnum provider : values()) {
            if (provider.getName().equalsIgnoreCase(name)) {
                return provider;
            }
        }
        return null;
    }
}

