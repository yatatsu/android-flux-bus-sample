package com.yatatsu.androidfluxeventbussample.event;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * EventEmitter (Dispatcher)
 *
 * @see <a href="https://facebook.github.io/flux/docs/dispatcher.html">
 *     Flux | Application Architecture for Building User Interfaces
 *     </a>
 */
public class EventEmitter {
    private static final Bus BUS = new Bus();
    private EventEmitter() {}

    private static class SingletonHolder {
        private static final EventEmitter instance = new EventEmitter();
    }

    public static EventEmitter get() {
        return SingletonHolder.instance;
    }

    public void on(Object obj) {
        BUS.register(obj);
    }

    public void off(Object obj) {
        BUS.unregister(obj);
    }

    public void emit(EventAction action) {
        post(action);
    }

    /**
     * Post Action in UI Thread.
     */
    private void post(final EventAction action) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BUS.post(action);
            }
        });
    }
}