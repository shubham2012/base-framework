package com.base.utils;

/** Enum of various Criteria Operations. */
public enum CriteriaOperation {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    LESS_THAN,
    LIKE,
    STARTS_WITH,
    ENDS_WITH,
    CONTAINS,
    GREATER_THAN_EQUALS,
    LESS_THAN_EQUALS;

    public static final String[] SIMPLE_OPERATION_SET = {"=", "!", ">", "<", "~"};

    public static final String ZERO_OR_MORE_REGEX = "*";

    public static final String OR_OPERATOR = "|";

    public static final String AND_OPERATOR = "&";

    public static final String LEFT_PARENTHESIS = "(";

    public static final String RIGHT_PARENTHESIS = ")";

    public static CriteriaOperation getSimpleOperation(final char input) {

        switch (input) {
            case '=':
                return EQUALS;
            case '!':
                return NOT_EQUALS;
            case '>':
                return GREATER_THAN;
            case '<':
                return LESS_THAN;
            case '~':
                return LIKE;
            default:
                return null;
        }
    }
}
