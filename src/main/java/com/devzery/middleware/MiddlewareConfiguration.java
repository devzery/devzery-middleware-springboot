package com.devzery.middleware;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import static org.zalando.logbook.core.Conditions.exclude;
import static org.zalando.logbook.core.Conditions.requestTo;

@Configuration
@ImportAutoConfiguration(classes = LogbookAutoConfiguration.class)
public class MiddlewareConfiguration implements ImportAware {

    private String apiKey;
    private String serverName;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(MiddlewareEnabled.class.getName()));
        if (attributes != null) {
            this.apiKey = attributes.getString("apiKey");
            this.serverName = attributes.getString("serverName");
        }        
    }

    @Bean
    FlaskApiProperties flaskApiProperties() {
        FlaskApiProperties flaskApiProperties = new FlaskApiProperties();
        flaskApiProperties.setApiKey(apiKey);
        flaskApiProperties.setSourceName(serverName);
        return flaskApiProperties;
    }

    @Bean
    Logbook logbook(){
        return Logbook.builder()
                .condition(exclude(
                    requestTo("/health/info"),
                    requestTo("/admin/**")
                ))
                .sink(new DefaultSink(
                        new PrincipalHttpLogFormatter(new JsonHttpLogFormatter()),
                        new FlaskApiClient(flaskApiProperties())
                ))
                .build();
    }
}
