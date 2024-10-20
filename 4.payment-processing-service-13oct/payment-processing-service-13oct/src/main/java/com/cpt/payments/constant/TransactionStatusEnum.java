package com.cpt.payments.constant;

public enum TransactionStatusEnum {
    CREATED(1, "CREATED"),
    INITIATED(2, "INITIATED"),
    PENDING(3, "PENDING"),
    APPROVED(4, "APPROVED"),
    SUCCESS(5, "SUCCESS"),
    FAILED(6, "FAILED");

    private final int id;
    private final String name;

    TransactionStatusEnum(int id, String name) {
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
    public static TransactionStatusEnum getById(int id) {
        for (TransactionStatusEnum status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return null;
    }

    // Method to get enum by name
    public static TransactionStatusEnum getByName(String name) {
        for (TransactionStatusEnum status : values()) {
            if (status.getName().equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;
    }
}
