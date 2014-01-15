package com.artisan.android.demo.activity;

import android.os.Bundle;
import android.view.View;

import com.artisan.android.demo.R;

public class HomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void navigateToStore(View target) {
		nextActivityIntent.setClass(this, StoreActivity.class);
		startActivity(nextActivityIntent);
	}

	public void navigateToAbout(View target) {
		nextActivityIntent.setClass(this, AboutActivity.class);
		startActivity(nextActivityIntent);
	}

	public void navigateToNews(View target) {
		nextActivityIntent.setClass(this, NewsActivity.class);
		startActivity(nextActivityIntent);
	}

	public void navigateToRequestDemo(View target) {
		nextActivityIntent.setClass(this, RequestDemoActivity.class);
		startActivity(nextActivityIntent);
	}
}
