package com.devzery.logging;


final class FlaskApiProperties {
    private static final String FLASK_API_URL = "https://server-v3-7qxc7hlaka-uc.a.run.app/api/add";
    private String sourceName = "";
    private String apiKey = "";

    public String getUrl() {
        return FLASK_API_URL;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}