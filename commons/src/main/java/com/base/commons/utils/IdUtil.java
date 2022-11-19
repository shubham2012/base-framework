package com.base.commons.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class IdUtil {

    /**
     * This method generates a 16 digit(total 19 including - at every interval of 4) unique id
     *
     * @return
     */
    public static String generateId() {
        String s =
                new SecureRandom()
                        .ints(0, 36)
                        .mapToObj(i -> Integer.toString(i, 36))
                        .map(String::toUpperCase)
                        .distinct()
                        .limit(16)
                        .collect(Collectors.joining())
                        .replaceAll("([A-Z0-9]{4})", "$1-")
                        .substring(0, 19);
        return s;
    }

    public static String generateIdWithOnlyCharacters() {
        String s =
                new SecureRandom()
                        .ints(0, 36)
                        .mapToObj(i -> Integer.toString(i, 36))
                        .map(String::toUpperCase)
                        .distinct()
                        .limit(16)
                        .collect(Collectors.joining())
                        .replaceAll("([A-Z0-9]{4})", "$1")
                        .substring(0, 16);
        return s;
    }

    public static String generateId(int length) {
        String s =
                new SecureRandom()
                        .ints(0, 36)
                        .mapToObj(i -> Integer.toString(i, 36))
                        .map(String::toUpperCase)
                        .distinct()
                        .limit(length)
                        .collect(Collectors.joining())
                        .replaceAll("([A-Z0-9]{4})", "$1")
                        .substring(0, length);
        return s;
    }
}
