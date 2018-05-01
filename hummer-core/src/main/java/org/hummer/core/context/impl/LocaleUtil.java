package org.hummer.core.context.impl;

import java.util.Locale;

public class LocaleUtil {
    public static Locale getLocale() {
        Locale locale;
        RequestContext context = ContextHolder.getRequestContext();

        if (context != null) {
            locale = context.getLocale();
        } else {
            locale = Locale.getDefault();
        }

        return locale;
    }
}
