package com.yatatsu.androidfluxeventbussample;


import com.squareup.otto.Bus;

public class BusProvider {

    private static final Bus BUS = new Bus();

    private static class SingletonHolder {
        private static final BusProvider instance = new BusProvider();
    }

    private BusProvider() {}

    public static BusProvider get() {
        return SingletonHolder.instance;
    }

    public void on(Object object) {
        BUS.register(object);
    }

    public void off(Object object) {
        BUS.unregister(object);
    }

    public void post(Object event) {
        BUS.post(event);
    }
}
