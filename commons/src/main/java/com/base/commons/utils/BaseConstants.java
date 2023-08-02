package com.base.commons.utils;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public interface BaseConstants {

    /** Setting MAX page fetch limit to 100 as this should not exceed more than 100 */
    Integer MAX_PAGE_FETCH_LIMIT = 100;

    /** Date format standard * */
    String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    /** Default value for data insertions * */
    String SYSTEM = "SYSTEM";

    /** Header name for sending request source * */
    String REQUEST_SOURCE = "REQUEST_SOURCE";

    DateTimeFormatter BASE_DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .optionalStart()
                    .appendPattern(" HH:mm")
                    .optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .toFormatter();
}
