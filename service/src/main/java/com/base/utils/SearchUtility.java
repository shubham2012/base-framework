package com.base.utils;

import static com.base.commons.utils.BaseConstants.MAX_PAGE_FETCH_LIMIT;

import com.base.commons.codes.BaseMessages;
import com.base.commons.exception.BadRequestException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/** This class is helping in initializing the default values for common search params and validate them */
@AllArgsConstructor
@Getter
public class SearchUtility {

    private Integer offset;
    private Integer limit;
    private Sort.Direction direction;
    private String sortBy;

    /**
     * This method going to initialize the default values for the parameters for which value is not present Offset
     * default = 0 limit default = 10 direction default = DESC sortBy default = id
     *
     * @return
     */
    public SearchUtility initializeDefault() {
        if (Objects.isNull(offset)) {
            offset = 0;
        }
        if (Objects.isNull(limit)) {
            limit = 10;
        }
        if (Objects.isNull(direction)) {
            direction = Sort.Direction.DESC;
        }
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "id";
        }
        return this;
    }

    /**
     * This method is going to validate the parameters Offset can not be less than 0 Limit can not be less than 1 Max
     * fetch limit is restricted to 100 records
     */
    public void validate() {
        if (offset < 0) {
            throw new BadRequestException(BaseMessages.OFFSET_LESS_THEN_0.get());
        }
        if (limit < 1) {
            throw new BadRequestException(BaseMessages.LIMIT_LESS_THEN_1.get());
        }
        if (limit > MAX_PAGE_FETCH_LIMIT) {
            throw new BadRequestException(BaseMessages.FETCH_LIMIT_EXCEEDED.get(MAX_PAGE_FETCH_LIMIT));
        }
    }
}
