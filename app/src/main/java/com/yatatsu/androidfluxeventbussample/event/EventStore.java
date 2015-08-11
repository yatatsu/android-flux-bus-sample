package com.yatatsu.androidfluxeventbussample.event;


public abstract class EventStore {

    private final EventEmitter eventEmitter;

    protected EventStore(EventEmitter eventEmitter) {
        this.eventEmitter = eventEmitter;
    }

    /**
     * Action emitted when this store changed.
     */
    protected abstract StoreChangeAction changeStoreAction();

    /**
     * emit self
     */
    protected void emitSelf() {
        eventEmitter.emit(changeStoreAction());
    }

    /**
     * Handling Action
     */
    public abstract void onAction(EventAction action);
}