package com.base.commons.response;

import com.fasterxml.jackson.annotation.JsonRootName;

/** For returning GENERIC RESPONSE */
@JsonRootName(value = "GenericResponse")
public class GenericResponse extends BaseResponse {

    public GenericResponse(Status status) {
        super(status);
    }
}
