package com.base.commons.enums;

import java.util.Arrays;
import org.springframework.util.StringUtils;

public enum RequestSource {
    CONSUMER_WEBAPP("consumer_webapp"),
    RETAILER_APP("retailer_app"),
    RETAILER_PWA("retailer_pwa"),
    SALES_APP("sales_app"),
    ADMIN_PANEL("admin_panel"),
    DELIVERY_APP("delivery_app"),
    DELIVERY_WEBAPP("delivery_webapp"),
    MERCHANT_APP("merchant_app"),
    CHATBOT("chatbot");

    public final String value;

    RequestSource(String value) {
        this.value = value;
    }

    public static RequestSource get(String val) {
        for (RequestSource source : RequestSource.values()) {
            if (source.value.equalsIgnoreCase(val)) {
                return source;
            }
        }
        return null;
    }

    public static RequestSource getOrDefault(String value, RequestSource defaultValue) {
        if (StringUtils.hasText(value)) {
            return Arrays.stream(RequestSource.values())
                    .filter(x -> x.value.equalsIgnoreCase(value))
                    .findFirst()
                    .orElse(defaultValue);
        }
        return defaultValue;
    }

    public String get() {
        return this.value;
    }
}
