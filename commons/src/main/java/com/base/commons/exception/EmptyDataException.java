package com.base.commons.exception;

import com.base.commons.enums.ResponseType;
import com.base.commons.response.Status;

/** When there is not data exist for the request then EmptyData Exception can be used */
public class EmptyDataException extends ServiceException {

    public EmptyDataException(String message) {
        super(message);
    }

    public EmptyDataException(Status err) {
        super(err);
    }

    public EmptyDataException(Status err, String param) {
        super(err, param);
    }

    public EmptyDataException(Status err, String... paramArray) {
        super(err, paramArray);
    }

    public EmptyDataException(BaseException err) {
        super(err);
    }

    public EmptyDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDataException(String message, Throwable cause, ResponseType responseType) {
        super(message, cause, responseType);
    }

    public EmptyDataException(Status err, Throwable cause) {
        super(err, cause);
    }
}
