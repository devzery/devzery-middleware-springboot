package com.devzery.middleware.adapter.jakarta;

import com.devzery.middleware.adapter.api.ServletResponseAdapter;
import jakarta.servlet.http.HttpServletResponse;

public class JakartaServletResponseAdapter implements ServletResponseAdapter {
    private final HttpServletResponse response;
    
    public JakartaServletResponseAdapter(HttpServletResponse response) {
        this.response = response;
    }
    
    @Override
    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }
    
    @Override
    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }
    
    @Override
    public void setStatus(int status) {
        response.setStatus(status);
    }
    
    @Override
    public Object getRawResponse() {
        return response;
    }
}