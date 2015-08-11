package com.yatatsu.androidfluxeventbussample.event;


public class StoreChangeAction<T extends EventStore> extends EventAction {

    private T store;

    public StoreChangeAction(T store) {
        this.store = store;
    }

    public T getStore() {
        return store;
    }
}