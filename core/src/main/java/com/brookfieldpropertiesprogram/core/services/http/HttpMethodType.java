package com.brookfieldpropertiesprogram.core.services.http;

/**
 * The Enum HttpMethodType.
 */
public enum HttpMethodType {

    /**
     * select.
     */
    GET,
    /**
     * edit.
     */
    POST,
    /**
     * add.
     */
    PUT,
    /**
     * DELETE.
     */
    DELETE;

    @Override
    public String toString() {
        return name();
    }
}
