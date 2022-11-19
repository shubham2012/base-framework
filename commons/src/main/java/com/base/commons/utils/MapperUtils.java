package com.base.commons.utils;

import com.base.commons.exception.ServiceException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Common Mapping Utilities to be used across the application */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapperUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Parses provided json string into an object of passed class definition T.
     *
     * @throws ServiceException if parsing fails.
     */
    public static <T> T toObject(final String json, final Class<T> clazz) throws ServiceException {

        if (json == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new ServiceException("Unable to parse Json String.", e);
        }
    }

    /**
     * Parses provided json string into an object of passed class definition T. Intended to be used for mapping json
     * collection to collection of objects. i.e. Mapping json list to list of objects of type T.
     *
     * @throws ServiceException if parsing fails.
     */
    public static <T> T toObject(final String json, final TypeReference<T> valueTypeRef) throws ServiceException {

        if (json == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (Exception e) {
            throw new ServiceException("Unable to parse Json String.", e);
        }
    }

    /** Parses provided JsonNode object into an object of passed class definition T. */
    public static <T> T toObject(final JsonNode jsonNode, final Class<T> clazz) throws ServiceException {

        if (jsonNode == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.treeToValue(jsonNode, clazz);
        } catch (Exception e) {
            throw new ServiceException("Unable to parse Json Tree.", e);
        }
    }

    /**
     * Converts given object to json string.
     *
     * @throws ServiceException if conversion fails.
     */
    public static String toJsonString(final Object object) throws ServiceException {

        if (object == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new ServiceException("Unable to convert Object to Json String.", e);
        }
    }

    /**
     * Converts passed object to an object of JsonNode. Intended to be used while handling large classes from which
     * little details are required.
     *
     * @throws ServiceException if conversion fails.
     */
    public static JsonNode toJsonNode(final Object object) throws ServiceException {

        if (object == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readTree(OBJECT_MAPPER.writeValueAsString(object));
        } catch (Exception e) {
            throw new ServiceException("Unable to convert Object to JsonNode", e);
        }
    }
}
