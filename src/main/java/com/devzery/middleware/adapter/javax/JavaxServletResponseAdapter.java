package com.devzery.middleware.adapter.javax;

import com.devzery.middleware.adapter.api.ServletResponseAdapter;
import javax.servlet.http.HttpServletResponse;

public class JavaxServletResponseAdapter implements ServletResponseAdapter {
    private final HttpServletResponse response;
    
    public JavaxServletResponseAdapter(HttpServletResponse response) {
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