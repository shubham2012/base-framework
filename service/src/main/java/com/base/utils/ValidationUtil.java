package com.base.utils;

import com.base.commons.exception.BadRequestException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtil {

    /**
     * Validates Request Entry and throws BadRequestException on error
     *
     * @param object
     * @param <T>
     */
    public static <T> void validate(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return;
        }
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            errorMessage.append(violation.getMessage());
            errorMessage.append("\n");
        }
        log.error("Validation failed. Entry: {}. Error: {}", object, errorMessage);
        throw new BadRequestException(errorMessage.toString());
    }
}
