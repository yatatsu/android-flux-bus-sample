package com.yatatsu.androidfluxeventbussample;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;


public class DataConnectionService extends IntentService {

    private static final String TAG = "DataConnectionService";
    private Subscription subscription;

    public DataConnectionService() {
        super("DataConnectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "start operation");

        startForeground(1, new Notification());
        subscription = getResultObservable().subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
                finishOperation();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                publishEvent(e.getMessage());
                finishOperation();
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext");
                publishEvent(s);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();

    }

    private void finishOperation() {
        Log.d(TAG, "finish operation");
        stopForeground(true);
    }

    void publishEvent(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.get().post(new ResultEvent(message));
            }
        });
    }

    private Observable<String> getResultObservable() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                // long mission
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Random rand = new Random();
                int n = rand.nextInt(100);
                if (n > 80) {
                    subscriber.onError(new RuntimeException("error 1"));
                } else if (n > 60) {
                    subscriber.onError(new RuntimeException("error 2"));
                } else {
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                }
            }
        });
    }
}
