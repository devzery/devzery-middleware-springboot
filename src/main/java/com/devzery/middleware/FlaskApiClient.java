package com.devzery.middleware;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

final class FlaskApiClient implements HttpLogWriter {

    private static final AsyncHttpClient httpClient = new DefaultAsyncHttpClient();

    private final FlaskApiProperties flaskApiProperties;

    FlaskApiClient(final FlaskApiProperties flaskApiProperties) {
        this.flaskApiProperties = flaskApiProperties;
    }

    private Map<String, Object> payload = new HashMap<>();

    @Override
    public void write(Precorrelation precorrelation, String request) throws IOException {
        // Construct the payload with the request details
        constructRequestPayload(request);
    }

    @Override
    public void write(Correlation correlation, String response) throws IOException {
        // Construct the payload with the response details
        constructResponsePayload(correlation, response);

        // Send the payload to the Flask API
        sendToFlaskAPIAsync();
    }

    private void constructRequestPayload(String request) throws IOException {
        payload.put("request", parseJsonToMap(request));
    }

    private void constructResponsePayload(Correlation correlation, String response) throws IOException {
        payload.put("response", parseJsonToMap(response));
        payload.put("elapsed_time", correlation.getDuration().toMillis());
    }

    private void sendToFlaskAPIAsync() throws IOException {
        RequestBuilder requestBuilder = new RequestBuilder()
                .setUrl(flaskApiProperties.getApiUrl())
                .setMethod("POST")
                .setBody(format(payload))
                .setCharset(StandardCharsets.UTF_8)
                .setHeader("Content-Type", "application/json")
                .setHeader("x-access-token", flaskApiProperties.getApiKey())
                .setHeader("source-name", flaskApiProperties.getSourceName());

        Request request = requestBuilder.build();

        ListenableFuture<Response> future = httpClient.executeRequest(request);
        // Attach a listener to handle the response asynchronously
        future.addListener(() -> {
            try {
                // Retrieve the response
                Response response = future.get();

                // Handle the response
                if (response.getStatusCode() == 200) {
                    System.out.println("Request successful!");
                    System.out.println("Response body: " + response.getResponseBody());
                } else {
                    System.err.println("Request failed with status code: " + response.getStatusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, null);
    }

    private String format(Map<String, Object> content) throws IOException {
        return new ObjectMapper().writeValueAsString(content);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJsonToMap(String json) throws IOException {
        return new ObjectMapper().readValue(json, Map.class);
    }
}
