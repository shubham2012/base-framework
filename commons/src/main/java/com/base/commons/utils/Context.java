package com.base.commons.utils;

import com.base.commons.enums.Locale;
import com.base.commons.enums.Role;

/** Context class keeps the context of current thread */
public class Context {

    private static final ThreadLocal<ContextInfo> threadLocalContextInfo = new ThreadLocal<>();

    /**
     * This method will return context info for the current thread. Using threadLocal for expose Context to be
     * accessible anywhere with for the current thread.
     *
     * @return
     */
    public static ContextInfo getContextInfo() {
        if (threadLocalContextInfo.get() != null) {
            return threadLocalContextInfo.get();
        } else {
            ContextInfo info =
                    new ContextInfo(
                            "System",
                            Role.USER.toString(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            Locale.EN,
                            null,
                            null,
                            null,
                            null);
            setContextInfo(info);
            return info;
        }
    }

    public static void setContextInfo(ContextInfo contextInfo) {
        threadLocalContextInfo.set(contextInfo);
    }
}
