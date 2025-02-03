package com.ebs.entities;

public enum UserType {
    A("ADMIN"),
    C("CONSUMER"),
    E("EMPLOYEE");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
