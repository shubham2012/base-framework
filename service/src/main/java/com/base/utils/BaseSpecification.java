package com.base.utils;

import com.base.commons.utils.BaseConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/** BaseSpecification class to convert BaseCriteria into predicates. */
public class BaseSpecification<T> implements Specification<T> {
    private final BaseCriteria criteria;

    public BaseSpecification(final BaseCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

        final Class<?> propertyType = root.get(criteria.getKey()).getJavaType();

        switch (criteria.getOperation()) {
            case EQUALS:
                if (Enum.class.isAssignableFrom(propertyType)) {
                    @SuppressWarnings("unchecked")
                    Class<Enum<?>> enumType = (Class<Enum<?>>) propertyType;
                    final Enum<?> enumValue =
                            Enum.valueOf(enumType.asSubclass(Enum.class), criteria.getValue().toString());
                    return builder.equal(root.get(criteria.getKey()), enumValue);
                } else if (LocalDate.class.isAssignableFrom(propertyType)) {
                    final LocalDate value = LocalDate.parse(criteria.getValue().toString(), DateTimeFormatter.ISO_DATE);
                    return builder.equal(root.get(criteria.getKey()), value);
                } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                    final LocalDateTime value =
                            LocalDateTime.parse(criteria.getValue().toString(), BaseConstants.BASE_DATE_TIME_FORMATTER);
                    return builder.equal(root.get(criteria.getKey()), value);
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
            case NOT_EQUALS:
                if (Enum.class.isAssignableFrom(propertyType)) {
                    @SuppressWarnings("unchecked")
                    Class<Enum<?>> enumType = (Class<Enum<?>>) propertyType;
                    final Enum<?> enumValue =
                            Enum.valueOf(enumType.asSubclass(Enum.class), criteria.getValue().toString());
                    return builder.notEqual(root.get(criteria.getKey()), enumValue);
                } else if (LocalDate.class.isAssignableFrom(propertyType)) {
                    final LocalDate value = LocalDate.parse(criteria.getValue().toString(), DateTimeFormatter.ISO_DATE);
                    return builder.notEqual(root.get(criteria.getKey()), value);
                } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                    final LocalDateTime value =
                            LocalDateTime.parse(criteria.getValue().toString(), BaseConstants.BASE_DATE_TIME_FORMATTER);
                    return builder.notEqual(root.get(criteria.getKey()), value);
                } else {
                    return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                }
            case GREATER_THAN:
                if (LocalDate.class.isAssignableFrom(propertyType)) {
                    final LocalDate value = LocalDate.parse(criteria.getValue().toString(), DateTimeFormatter.ISO_DATE);
                    return builder.greaterThan(root.get(criteria.getKey()), value);
                } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                    final LocalDateTime value =
                            LocalDateTime.parse(criteria.getValue().toString(), BaseConstants.BASE_DATE_TIME_FORMATTER);
                    return builder.greaterThan(root.get(criteria.getKey()), value);
                } else {
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN:
                if (LocalDate.class.isAssignableFrom(propertyType)) {
                    final LocalDate value = LocalDate.parse(criteria.getValue().toString(), DateTimeFormatter.ISO_DATE);
                    return builder.lessThan(root.get(criteria.getKey()), value);
                } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                    final LocalDateTime value =
                            LocalDateTime.parse(criteria.getValue().toString(), BaseConstants.BASE_DATE_TIME_FORMATTER);
                    return builder.lessThan(root.get(criteria.getKey()), value);
                } else {
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case GREATER_THAN_EQUALS:
                if (LocalDate.class.isAssignableFrom(propertyType)) {
                    final LocalDate value = LocalDate.parse(criteria.getValue().toString(), DateTimeFormatter.ISO_DATE);
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), value);
                } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                    final LocalDateTime value =
                            LocalDateTime.parse(criteria.getValue().toString(), BaseConstants.BASE_DATE_TIME_FORMATTER);
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), value);
                } else {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN_EQUALS:
                if (LocalDate.class.isAssignableFrom(propertyType)) {
                    final LocalDate value = LocalDate.parse(criteria.getValue().toString(), DateTimeFormatter.ISO_DATE);
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), value);
                } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                    final LocalDateTime value =
                            LocalDateTime.parse(criteria.getValue().toString(), BaseConstants.BASE_DATE_TIME_FORMATTER);
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), value);
                } else {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LIKE:
                return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
                // TODO: Can be deprecated.
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}
