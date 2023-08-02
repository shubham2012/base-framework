package com.base.utils;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/** Base specification builder to build Specification out of Postfix stack of BaseCriteria and "AND"/"OR" Operators. */
@NoArgsConstructor
public class BaseSpecificationBuilder<T> {
    public Specification<T> build(
            final Deque<?> postFixedExprStack, final Function<BaseCriteria, BaseSpecification<T>> converter) {

        Deque<Specification<T>> specStack = new ArrayDeque<>();

        Collections.reverse((List<?>) postFixedExprStack);

        while (!postFixedExprStack.isEmpty()) {
            Object mayBeOperand = postFixedExprStack.pop();

            if (!(mayBeOperand instanceof String)) {
                specStack.push(converter.apply((BaseCriteria) mayBeOperand));
            } else {
                Specification<T> operand1 = specStack.pop();
                Specification<T> operand2 = specStack.pop();
                if (mayBeOperand.equals(CriteriaOperation.AND_OPERATOR))
                    specStack.push(Specification.where(operand1).and(operand2));
                else if (mayBeOperand.equals(CriteriaOperation.OR_OPERATOR))
                    specStack.push(Specification.where(operand1).or(operand2));
            }
        }

        return specStack.pop();
    }
}
