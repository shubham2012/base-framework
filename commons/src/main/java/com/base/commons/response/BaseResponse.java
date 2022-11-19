package com.base.commons.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse implements Serializable {
    private static final long serialVersionUID = 766737479775870601L;
    private Status status;
    private PageDetail pageDetail;
    private Long totalCount;

    public BaseResponse(PageDetail pageDetail, Long totalCount) {
        this.pageDetail = pageDetail;
        this.totalCount = totalCount;
    }

    public BaseResponse(Status status) {
        this.status = status;
    }
}
