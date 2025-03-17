package com.devzery.middleware.adapter.api;

/**
 * Interface for servlet response functionality that works across
 * javax and jakarta packages
 */
public interface ServletResponseAdapter {
    void setHeader(String name, String value);
    void setContentType(String contentType);
    void setStatus(int status);
    Object getRawResponse();
}