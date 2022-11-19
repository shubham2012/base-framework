package com.base.commons.response;

import com.base.commons.enums.ResponseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {
    private ResponseType responseType;
    private String message;

    public Status(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }
}
