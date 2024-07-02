package com.devzery.middleware;

import java.lang.annotation.*;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(MiddlewareImportSelector.class)
public @interface MiddlewareEnabled {
    String apiKey() default "";
    String serverName() default "";
}