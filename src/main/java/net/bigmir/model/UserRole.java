package net.bigmir.model;

public enum UserRole {
    BANNED, USER, ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
