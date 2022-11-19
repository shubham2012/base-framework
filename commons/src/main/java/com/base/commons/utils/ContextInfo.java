package com.base.commons.utils;

import com.base.commons.enums.APIVersion;
import com.base.commons.enums.Locale;
import com.base.commons.enums.RequestSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContextInfo {

    private String sub;
    private String role;
    private String id;
    private String eventId;
    private APIVersion apiVersion;
    private String serverDetails;
    private String service;
    private Locale locale;
    private String remoteAddress;
    private RequestSource requestSource;
    private String appVersion;
    private Object remarks;
}
