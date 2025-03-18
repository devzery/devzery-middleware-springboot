package com.devzery.middleware;

import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import java.io.IOException;
import java.util.Map;

final class PrincipalHttpLogFormatter implements HttpLogFormatter {

    private final JsonHttpLogFormatter delegate;

    PrincipalHttpLogFormatter(final JsonHttpLogFormatter delegate) {
        this.delegate = delegate;
    }

    @Override
    public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {
        final Map<String, Object> content = delegate.prepare(precorrelation, request);
        content.remove("correlation");
        content.remove("remote");
        content.remove("origin");
        content.remove("protocol");
        content.remove("type");
        content.remove("scheme");
        content.remove("port");
        content.remove("host");
        content.remove("path");
        content.remove("uri");
        String path = request.getPath();
        String query = request.getQuery();
        if (query != null && !query.isEmpty()) {
            path += "?" + query;
        }
        content.put("path", path);
        return delegate.format(content);
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {
        final Map<String, Object> content = delegate.prepare(correlation, response);

        Object bodyContent = content.get("body");
        
        content.remove("origin");
        content.remove("type");
        content.remove("correlation");
        content.remove("protocol");
        content.remove("duration");
        content.remove("status");
        content.remove("headers");
        content.remove("body");
        content.put("status_code", response.getStatus());
        content.put("content", bodyContent);
        return delegate.format(content);
    }
}
