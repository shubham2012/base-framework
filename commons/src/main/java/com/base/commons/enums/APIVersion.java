package com.base.commons.enums;

import java.util.Arrays;
import org.springframework.util.StringUtils;

public enum APIVersion {
    /** API versions from request. Add more values here as we introduce new API versions */
    V1_0_0("1.0.0"),
    V1_1_0("1.1.0"),
    V1_2_0("1.2.0"),
    V2_0_0("2.0.0"),
    V2_1_0("2.1.0"),
    V3_0_0("3.0.0"),
    V4_0_0("4.0.0");
    public final String value;

    APIVersion(String value) {
        this.value = value;
    }

    public static APIVersion getFromStringOrDefault(String text, APIVersion defaultValue) {
        if (StringUtils.hasText(text)) {
            return Arrays.stream(APIVersion.values())
                    .filter(x -> x.value.equalsIgnoreCase(text))
                    .findFirst()
                    .orElse(defaultValue);
        }
        return defaultValue;
    }
}
