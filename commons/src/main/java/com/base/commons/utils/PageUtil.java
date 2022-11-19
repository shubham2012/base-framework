package com.base.commons.utils;

import com.base.commons.response.PageDetail;

public class PageUtil {

    public static PageDetail getPage(Integer totalPages, Integer offset, Integer limit) {
        return new PageDetail(totalPages, (int) Math.floor((double) offset / (double) limit) + 1);
    }
}
