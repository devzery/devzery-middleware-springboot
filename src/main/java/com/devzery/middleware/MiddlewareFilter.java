package com.devzery.middleware;

import com.devzery.middleware.adapter.AdapterFactory;
import com.devzery.middleware.adapter.CompatibleOncePerRequestFilter;
import com.devzery.middleware.adapter.api.ServletRequestAdapter;
// import com.devzery.middleware.adapter.api.ServletResponseAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.lang.reflect.Method;

/**
 * Filter that captures HTTP requests and responses and logs them using the adapter pattern
 * for cross-compatibility between javax and jakarta
 */
public class MiddlewareFilter extends CompatibleOncePerRequestFilter {
    
    private final FlaskApiClient apiClient;
    
    public MiddlewareFilter(FlaskApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    protected void doFilterCommon(Object request, Object response, Object filterChain) 
            throws IOException {
        try {
            // Create adapters
            ServletRequestAdapter requestAdapter = AdapterFactory.createRequestAdapter(request);
            // ServletResponseAdapter responseAdapter = AdapterFactory.createResponseAdapter(response);
            
            // Capture request details
            String method = requestAdapter.getMethod();
            String path = requestAdapter.getRequestURI();
            String userAgent = requestAdapter.getHeader("User-Agent");
            
            // Log request timing
            Instant start = Instant.now();
            
            // Continue the filter chain with reflection
            try {
                Class<?> filterChainClass = filterChain.getClass();
                Method doFilterMethod = filterChainClass.getMethod(
                    "doFilter", request.getClass(), response.getClass());
                doFilterMethod.invoke(filterChain, request, response);
            } catch (Exception e) {
                throw new IOException("Filter invocation failed", e);
            }
            
            // Calculate request duration
            Duration duration = Duration.between(start, Instant.now());
            
            // Send data to Flask API - use the correct status code accessor
            int status = -1;
            try {
                Method getStatusMethod = response.getClass().getMethod("getStatus");
                status = (int) getStatusMethod.invoke(response);
            } catch (Exception e) {
                logger.warn("Could not get response status", e);
            }
            
            apiClient.logRequest(method, path, userAgent, status, duration.toMillis());
            
        } catch (Exception e) {
            logger.error("Error in middleware filter", e);
            throw new IOException("Middleware processing error", e);
        }
    }
}