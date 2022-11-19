package com.base.commons.enums;

public enum Locale {
    EN("en");

    public final String value;

    Locale(String value) {
        this.value = value;
    }

    public String get() {
        return this.value;
    }

    public static Locale get(String val) {
        for (Locale locale : Locale.values()) {
            if (locale.value.equalsIgnoreCase(val)) {
                return locale;
            }
        }
        return null;
    }
}
