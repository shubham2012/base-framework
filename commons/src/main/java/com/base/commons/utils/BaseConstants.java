package com.base.commons.utils;

public interface BaseConstants {

    /** Setting MAX page fetch limit to 100 as this should not exceed more than 100 */
    Integer MAX_PAGE_FETCH_LIMIT = 100;

    /** Date format standard * */
    String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    /** Default value for data insertions * */
    String SYSTEM = "SYSTEM";

    /** Header name for sending request source * */
    String REQUEST_SOURCE = "REQUEST_SOURCE";
}
