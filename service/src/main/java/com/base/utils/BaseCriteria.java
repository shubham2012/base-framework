package com.base.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Class to encapsulate key, operation and value into BaseCriteria. */
@Getter
@Setter
@NoArgsConstructor
public class BaseCriteria {
    private String key;

    private CriteriaOperation operation;

    private Object value;

    public BaseCriteria(
            final String key, final String operation, final String prefix, final String value, final String suffix) {

        CriteriaOperation op = CriteriaOperation.getSimpleOperation(operation.charAt(0));

        if (op != null) {
            if (op == CriteriaOperation.EQUALS) {
                final boolean startWithAsterisk =
                        prefix != null && prefix.contains(CriteriaOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(CriteriaOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = CriteriaOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = CriteriaOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = CriteriaOperation.STARTS_WITH;
                }
            } else if (op == CriteriaOperation.GREATER_THAN) {
                if ("=".equals(prefix)) {
                    op = CriteriaOperation.GREATER_THAN_EQUALS;
                }
            } else if (op == CriteriaOperation.LESS_THAN) {
                if ("=".equals(prefix)) {
                    op = CriteriaOperation.LESS_THAN_EQUALS;
                }
            }
        }

        this.key = key;
        this.operation = op;
        this.value = value;
    }

    @Override
    public String toString() {

        return key + " " + operation.name() + " " + value;
    }
}
