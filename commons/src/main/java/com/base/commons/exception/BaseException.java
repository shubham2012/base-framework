package com.base.commons.exception;

import com.base.commons.enums.ResponseType;
import com.base.commons.response.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends Exception {
    private static final long serialVersionUID = -3662322486315870601L;
    private ResponseType responseType;
    private Status statusCode;

    /**
     * Base Exception with only message As this will cause Error so responseType will be set to ERROR
     *
     * @param message
     */
    public BaseException(String message) {
        super(message);
        this.responseType = ResponseType.ERROR;
    }

    /** @param status */
    public BaseException(Status status) {
        this(status.getMessage());
        this.statusCode = status;
    }

    /**
     * @param message
     * @param cause
     */
    public BaseException(String message, Throwable cause) {
        this(message, cause, null);
    }

    /**
     * Consumes message, cause and Response type If response type coming as null then we will set it to ERROR by default
     *
     * @param message
     * @param cause
     * @param responseType
     */
    public BaseException(String message, Throwable cause, ResponseType responseType) {
        super(message, cause);
        this.responseType = responseType == null ? ResponseType.ERROR : responseType;
    }

    /**
     * @param status
     * @param cause
     */
    public BaseException(Status status, Throwable cause) {
        this(status.getMessage(), cause);
        this.statusCode = status;
    }
}
