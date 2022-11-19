package com.base.commons.response;

import com.base.commons.entry.BaseEntry;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PageResponse<R extends BaseEntry> {
    private PageDetail pageDetail;
    private Long totalCount;
    private List<R> data;
}
