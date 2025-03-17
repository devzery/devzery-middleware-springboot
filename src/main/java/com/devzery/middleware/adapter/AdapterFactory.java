package com.devzery.middleware.adapter;

import com.devzery.middleware.adapter.api.ServletRequestAdapter;
import com.devzery.middleware.adapter.api.ServletResponseAdapter;
import org.springframework.boot.SpringBootVersion;

public class AdapterFactory {
    private static final boolean IS_JAKARTA;
    
    static {
        String version = SpringBootVersion.getVersion();
        IS_JAKARTA = version != null && version.startsWith("3");
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ServletRequestAdapter createRequestAdapter(Object request) {
        if (IS_JAKARTA) {
            try {
                Class jakartaRequestClass = Class.forName("jakarta.servlet.http.HttpServletRequest");
                if (jakartaRequestClass.isInstance(request)) {
                    Class adapterClass = Class.forName("com.devzery.middleware.adapter.jakarta.JakartaServletRequestAdapter");
                    return (ServletRequestAdapter) adapterClass.getConstructor(jakartaRequestClass)
                            .newInstance(request);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Jakarta request adapter", e);
            }
        } else {
            try {
                Class javaxRequestClass = Class.forName("javax.servlet.http.HttpServletRequest");
                if (javaxRequestClass.isInstance(request)) {
                    Class adapterClass = Class.forName("com.devzery.middleware.adapter.javax.JavaxServletRequestAdapter");
                    return (ServletRequestAdapter) adapterClass.getConstructor(javaxRequestClass)
                            .newInstance(request);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Javax request adapter", e);
            }
        }
        throw new IllegalArgumentException("Unsupported request type: " + request.getClass());
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ServletResponseAdapter createResponseAdapter(Object response) {
        if (IS_JAKARTA) {
            try {
                Class jakartaResponseClass = Class.forName("jakarta.servlet.http.HttpServletResponse");
                if (jakartaResponseClass.isInstance(response)) {
                    Class adapterClass = Class.forName("com.devzery.middleware.adapter.jakarta.JakartaServletResponseAdapter");
                    return (ServletResponseAdapter) adapterClass.getConstructor(jakartaResponseClass)
                            .newInstance(response);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Jakarta response adapter", e);
            }
        } else {
            try {
                Class javaxResponseClass = Class.forName("javax.servlet.http.HttpServletResponse");
                if (javaxResponseClass.isInstance(response)) {
                    Class adapterClass = Class.forName("com.devzery.middleware.adapter.javax.JavaxServletResponseAdapter");
                    return (ServletResponseAdapter) adapterClass.getConstructor(javaxResponseClass)
                            .newInstance(response);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Javax response adapter", e);
            }
        }
        throw new IllegalArgumentException("Unsupported response type: " + response.getClass());
    }
}
