package com.devzery.middleware.adapter.jakarta;

import com.devzery.middleware.adapter.api.ServletRequestAdapter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class JakartaServletRequestAdapter implements ServletRequestAdapter {
    private final HttpServletRequest request;
    
    public JakartaServletRequestAdapter(HttpServletRequest request) {
        this.request = request;
    }
    
    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }
    
    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }
    
    @Override
    public String getMethod() {
        return request.getMethod();
    }
    
    @Override
    public String getRequestURI() {
        return request.getRequestURI();
    }
    
    @Override
    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }
    
    @Override
    public Object getRawRequest() {
        return request;
    }
}