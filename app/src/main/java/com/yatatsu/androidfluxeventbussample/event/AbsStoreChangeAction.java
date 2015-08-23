package com.yatatsu.androidfluxeventbussample.event;


public abstract class AbsStoreChangeAction<T extends EventStore> extends EventAction {

    private T store;

    public AbsStoreChangeAction(T store) {
        this.store = store;
    }

    public T getStore() {
        return store;
    }
}