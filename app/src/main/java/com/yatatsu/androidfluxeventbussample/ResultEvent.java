package com.yatatsu.androidfluxeventbussample;


public class ResultEvent {
    private final String message;

    public ResultEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
