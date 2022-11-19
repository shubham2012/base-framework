package com.base.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/** We define all the possible roles here for the ULA */
public enum Role {
    USER("user"),
    ADMIN("admin"),
    SERVICE("service"),
    GUEST("guest"),
    THIRD_PARTY("third_party"),
    SUPER_ADMIN("super_admin");

    public final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    public String get() {
        return this.value;
    }

    public static Role get(String val) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(val)) {
                return role;
            }
        }
        return null;
    }
}
