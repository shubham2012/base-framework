package com.base.commons.codes;

import com.base.commons.enums.ResponseType;
import com.base.commons.response.Status;

/** Contains base generic error codes to be used by any service */
public class BaseErrorCodes extends Status {

    public BaseErrorCodes(String message) {
        super(ResponseType.ERROR, message);
    }

    public static final Status GENERIC_ERROR = new BaseErrorCodes("GENERIC_ERROR_OCCURRED");
    public static final Status NO_DATA_FOUND = new BaseErrorCodes("NO_DATA_FOUND");
    public static final Status INVALID_REQUEST = new BaseErrorCodes("INVALID_REQUEST");
    public static final Status VALIDATION_FAILED = new BaseErrorCodes("VALIDATION_FAILED");
}
