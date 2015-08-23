package com.yatatsu.androidfluxeventbussample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Subscribe;
import com.yatatsu.androidfluxeventbussample.event.EventEmitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long DEFAULT_ID = -1L;
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        taskId = DEFAULT_ID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventEmitter emitter = EventEmitter.get();
        updateWithEventStore(ResultEventStore.get(emitter));
        emitter.on(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventEmitter.get().off(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                taskId = System.currentTimeMillis();
                startService(SomeLongTaskService.createIntent(getApplicationContext(), taskId));
                break;
        }
    }

    @Subscribe
    public void subscribeResultEvent(ResultEventStore.StoreChangeAction action) {
        updateWithEventStore(action.getStore());
    }

    private void updateWithEventStore(ResultEventStore eventStore) {
        if (eventStore.getTaskId() == taskId) {
            showMessage(eventStore.getMessage());
        }
    }

    private void showMessage(String message) {
        View main = findViewById(R.id.main_content);
        Snackbar.make(main, message, Snackbar.LENGTH_LONG).show();
        // for showing once.
        taskId = DEFAULT_ID;
    }
}
