package com.base.commons.exception;

import com.base.commons.enums.ResponseType;
import com.base.commons.response.Status;
import java.text.MessageFormat;

/**
 * For Any service level exception in Any kind of business logic error or violations ServiceException should be thrown
 */
public class ServiceException extends BaseException {
    private static final long serialVersionUID = -548897963207223462L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Status err) {
        super(err);
    }

    public ServiceException(Status err, String param) {
        super(MessageFormat.format(err.getMessage(), param));
    }

    public ServiceException(Status err, String... paramArray) {
        super(MessageFormat.format(err.getMessage(), paramArray));
    }

    public ServiceException(BaseException err) {
        super(err.getMessage(), err.getCause());
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, ResponseType responseType) {
        super(message, cause, responseType);
    }

    public ServiceException(Status err, Throwable cause) {
        super(err, cause);
    }
}
