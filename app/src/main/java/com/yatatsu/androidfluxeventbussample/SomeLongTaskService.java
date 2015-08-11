package com.yatatsu.androidfluxeventbussample;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yatatsu.androidfluxeventbussample.event.EventEmitter;

import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * Dummy Task Service
 */
public class SomeLongTaskService extends IntentService {

    private static final String TAG = "SomeLongTaskService";
    private static final String ARGS_TASK_ID = "_ARGS_TASK_ID_";
    private Subscription subscription;
    private ResultEventStore eventStore;

    public static Intent createIntent(Context context, long taskId) {
        return new Intent(context, SomeLongTaskService.class)
                .putExtra(ARGS_TASK_ID, taskId);
    }

    public SomeLongTaskService() {
        super("SomeLongTaskService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventEmitter emitter = EventEmitter.get();
        eventStore = ResultEventStore.get(emitter);
        emitter.on(eventStore);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "start operation");

        final long taskId = intent.getLongExtra(ARGS_TASK_ID, 0);

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
                publishEvent(taskId, e.getMessage());
                finishOperation();
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext");
                publishEvent(taskId, s);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (subscription != null) {
            subscription.unsubscribe();
        }
        EventEmitter.get().off(eventStore);
        super.onDestroy();
    }

    private void finishOperation() {
        Log.d(TAG, "finish operation");
        stopForeground(true);
    }

    private void publishEvent(final long taskId, final String message) {
        EventEmitter.get().emit(new ResultEventAction(taskId, message));
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
