package com.base.commons.exception;

public class RecordExistsException extends BaseException {

    public RecordExistsException(String message) {
        super(message);
    }

    public RecordExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
