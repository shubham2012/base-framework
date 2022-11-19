package com.base.service.config.auth;

import com.base.commons.enums.Role;
import com.base.commons.exception.UnauthorizedException;
import com.base.commons.utils.Context;
import com.base.commons.utils.ContextInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizeImpl {

    public void authorize(Role[] roles) {
        ContextInfo contextInfo = Context.getContextInfo();
        if (StringUtils.isEmpty(contextInfo.getRole())) {
            String message = "User not authorized to perform this operation.";
            log.warn(message);
            throw new UnauthorizedException(message);
        }
    }
}
