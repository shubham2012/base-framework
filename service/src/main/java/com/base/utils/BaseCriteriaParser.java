package com.base.utils;

import static com.base.utils.CriteriaOperation.*;

import com.google.common.base.Joiner;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Base Criteria Parser class to parse search params. */
public class BaseCriteriaParser {
    /** Constant Map defining precedence of "AND" and "OR" operators. */
    private static final Map<String, Integer> AND_OR_PRECEDENCE_MAP = Map.of(OR_OPERATOR, 1, AND_OPERATOR, 2);

    /** Regex constant to parse individual operations, */
    private static final Pattern CRITERIA_REGEX =
            Pattern.compile(
                    "^(\\w+?)("
                            + Joiner.on("|").join(SIMPLE_OPERATION_SET)
                            + ")(\\p{Punct}?)(\\w+|[0-9]+\\.?[0-9]+|[0-9-]+)(\\p{Punct}?)$");

    private static boolean isHigherPrecedenceOperator(final String currOp, final String prevOp) {

        return AND_OR_PRECEDENCE_MAP.containsKey(prevOp)
                && AND_OR_PRECEDENCE_MAP.containsKey(currOp)
                && AND_OR_PRECEDENCE_MAP.get(prevOp) >= AND_OR_PRECEDENCE_MAP.get(currOp);
    }

    /**
     * Parse the search param into postfix deque of tokens and operations. This would be converted into specifications.
     *
     * @param searchParam not empty
     * @return postfix deque of tokens and operations.
     */
    public Deque<?> parse(final String searchParam) {

        final Deque<Object> output = new LinkedList<>();
        final Deque<String> tokens = new LinkedList<>();

        StringBuilder buffer = new StringBuilder();

        for (final String charStr : searchParam.split("")) {
            switch (charStr) {
                case OR_OPERATOR:
                case AND_OPERATOR:
                    buffer = offloadBuffer(output, buffer);
                    while (!tokens.isEmpty() && isHigherPrecedenceOperator(charStr, tokens.peek())) {
                        output.push(OR_OPERATOR.equalsIgnoreCase(tokens.pop()) ? OR_OPERATOR : AND_OPERATOR);
                    }
                    tokens.push(OR_OPERATOR.equalsIgnoreCase(charStr) ? OR_OPERATOR : AND_OPERATOR);
                    break;
                case LEFT_PARENTHESIS:
                    buffer = offloadBuffer(output, buffer);
                    tokens.push(LEFT_PARENTHESIS);
                    break;
                case RIGHT_PARENTHESIS:
                    buffer = offloadBuffer(output, buffer);
                    while (!tokens.isEmpty() && !tokens.peek().equals(LEFT_PARENTHESIS)) {
                        output.push(tokens.pop());
                    }
                    tokens.pop();
                    break;
                default:
                    buffer.append(charStr);
                    break;
            }
        }

        offloadBuffer(output, buffer);

        while (!tokens.isEmpty()) {
            output.push(tokens.pop());
        }

        return output;
    }

    /**
     * Offload buffer to output and return new buffer.
     *
     * @param output postfix deque of tokens and operations.
     * @param buffer buffer of collected tokens.
     * @return new buffer
     */
    private static StringBuilder offloadBuffer(final Deque<Object> output, StringBuilder buffer) {

        if (buffer.length() != 0) {
            final String string = buffer.toString();
            Matcher matcher = CRITERIA_REGEX.matcher(string);
            while (matcher.find()) {
                output.push(
                        new BaseCriteria(
                                matcher.group(1),
                                matcher.group(2),
                                matcher.group(3),
                                matcher.group(4),
                                matcher.group(5)));
            }
            buffer = new StringBuilder();
        }
        return buffer;
    }
}
