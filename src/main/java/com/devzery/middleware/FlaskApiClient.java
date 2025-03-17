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
    
    /**
     * Direct method to log request/response data from our adapter-based filter
     */
    public void logRequest(String method, String path, String userAgent, int statusCode, long durationMillis) {
        try {
            Map<String, Object> payload = new HashMap<>();
            
            // Build request data
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("method", method);
            requestData.put("path", path);
            requestData.put("user_agent", userAgent);
            payload.put("request", requestData);
            
            // Build response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status_code", statusCode);
            payload.put("response", responseData);
            
            // Add duration
            payload.put("elapsed_time", durationMillis);
            
            // Send to API
            sendPayloadToFlaskAPI(payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constructRequestPayload(String request) throws IOException {
        payload.put("request", parseJsonToMap(request));
    }

    private void constructResponsePayload(Correlation correlation, String response) throws IOException {
        payload.put("response", parseJsonToMap(response));
        payload.put("elapsed_time", correlation.getDuration().toMillis());
    }

    private void sendToFlaskAPIAsync() throws IOException {
        sendPayloadToFlaskAPI(payload);
    }
    
    private void sendPayloadToFlaskAPI(Map<String, Object> payload) throws IOException {
        RequestBuilder requestBuilder = new RequestBuilder()
                .setUrl(flaskApiProperties.getUrl())
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
                if (response.getStatusCode() != 200) {
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