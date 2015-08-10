package com.yatatsu.androidfluxeventbussample;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.get().on(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.get().off(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startService(new Intent(getApplicationContext(), DataConnectionService.class));
                break;
        }
    }

    @Subscribe
    public void subscribeResultEvent(ResultEvent event) {
        View main = findViewById(R.id.main_content);
        Snackbar.make(main, event.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}
