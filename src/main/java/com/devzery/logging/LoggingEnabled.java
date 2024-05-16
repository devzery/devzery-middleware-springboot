package com.devzery.logging;

import java.lang.annotation.*;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(LoggingImportSelector.class)
public @interface LoggingEnabled {
    String apiKey() default "";
    String sourceName() default "";
}