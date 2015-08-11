package com.yatatsu.androidfluxeventbussample;


import com.yatatsu.androidfluxeventbussample.event.EventAction;

public class ResultEventAction extends EventAction {
    private final long taskId;
    private final String message;

    public ResultEventAction(long taskId, String message) {
        this.taskId = taskId;
        this.message = message;
    }

    public long getTaskId() {
        return taskId;
    }

    public String getMessage() {
        return message;
    }
}
