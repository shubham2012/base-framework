package com.base.service.config.auth;

import com.base.commons.enums.Role;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Authorize {
    Role[] roles() default {Role.ADMIN, Role.USER, Role.SERVICE};
}
