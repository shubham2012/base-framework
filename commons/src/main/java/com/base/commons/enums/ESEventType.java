package com.base.commons.enums;

/** Events used by ES service to handle update, insert and delete of data */
public enum ESEventType {
    ELASTIC_SEARCH_INSERT,
    ELASTIC_SEARCH_UPDATE,
    ELASTIC_SEARCH_DELETE,
    ELASTIC_SEARCH_INSERT_OR_UPDATE,
    ELASTIC_SEARCH_BULK_INSERT,
    ELASTIC_SEARCH_BULK_UPDATE,
    UPDATE_BY_QUERY;
}
