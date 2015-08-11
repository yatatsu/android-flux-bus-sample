package com.yatatsu.androidfluxeventbussample;

import com.squareup.otto.Subscribe;
import com.yatatsu.androidfluxeventbussample.event.EventAction;
import com.yatatsu.androidfluxeventbussample.event.EventEmitter;
import com.yatatsu.androidfluxeventbussample.event.EventStore;
import com.yatatsu.androidfluxeventbussample.event.StoreChangeAction;


public class ResultEventStore extends EventStore {

    private long taskId;
    private String message;

    private static ResultEventStore instance;

    protected ResultEventStore(EventEmitter eventEmitter) {
        super(eventEmitter);
    }

    public static ResultEventStore get(EventEmitter eventEmitter) {
        if (instance == null) {
            instance = new ResultEventStore(eventEmitter);
        }
        return instance;
    }

    @Override
    protected StoreChangeAction changeStoreAction() {
        return new StoreChangeAction<>(this);
    }

    @Subscribe
    @Override
    public void onAction(EventAction action) {
        if (action instanceof ResultEventAction) {
            ResultEventAction resultEventAction = (ResultEventAction) action;
            taskId = resultEventAction.getTaskId();
            message = resultEventAction.getMessage();
            emitSelf();
        }
    }

    public long getTaskId() {
        return taskId;
    }

    public String getMessage() {
        return message;
    }
}
