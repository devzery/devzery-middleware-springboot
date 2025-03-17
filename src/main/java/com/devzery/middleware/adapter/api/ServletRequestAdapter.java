package com.devzery.middleware.adapter.api;

import java.util.Map;

/**
 * Interface for servlet request functionality that works across
 * javax and jakarta packages
 */
public interface ServletRequestAdapter {
    String getHeader(String name);
    Map<String, String[]> getParameterMap();
    String getParameter(String name);
    String getMethod();
    String getRequestURI();
    String getRemoteAddr();
    Object getRawRequest();
}