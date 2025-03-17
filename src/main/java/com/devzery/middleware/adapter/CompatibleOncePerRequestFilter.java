package com.devzery.middleware.adapter;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * A version of OncePerRequestFilter that works with both javax and jakarta servlet APIs
 */
public abstract class CompatibleOncePerRequestFilter extends OncePerRequestFilter {

    /**
     * Common method to be implemented by subclasses
     */
    protected abstract void doFilterCommon(Object request, Object response, Object filterChain) 
            throws IOException;

    // Override both versions of doFilterInternal to handle both servlet APIs
    // These methods delegate to our custom doFilterInternal method
    
    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, 
                                   javax.servlet.http.HttpServletResponse response, 
                                   javax.servlet.FilterChain filterChain) 
            throws javax.servlet.ServletException, IOException {
        doFilterCommon((Object)request, (Object)response, (Object)filterChain);
    }
    
    // This method will be used in Jakarta environments
    // It's dynamically called via reflection when running in a Jakarta context
    public void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, 
                               jakarta.servlet.http.HttpServletResponse response, 
                               jakarta.servlet.FilterChain filterChain) 
            throws jakarta.servlet.ServletException, IOException {
        doFilterCommon((Object)request, (Object)response, (Object)filterChain);
    }
}