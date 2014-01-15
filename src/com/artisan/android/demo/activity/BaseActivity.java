package com.artisan.android.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.artisan.android.demo.R;
import com.artisan.android.demo.service.LocalStorageManager;

public class BaseActivity extends Activity {

    protected final LocalStorageManager storageManager = new LocalStorageManager();
    protected final Intent nextActivityIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        storageManager.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        storageManager.stop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.action_bar_checkout:
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
            return true;
        default:
            return false;
        }
    }

    protected int getOptionsMenuResource() {
        return R.menu.action_bar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(getOptionsMenuResource(), menu);
        return true;
    }

}
