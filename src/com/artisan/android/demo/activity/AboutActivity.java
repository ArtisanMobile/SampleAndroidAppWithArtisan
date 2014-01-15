package com.artisan.android.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.artisan.android.demo.R;
import com.artisan.powerhooks.PowerHookManager;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, PowerHookManager.getVariableValue("WelcomeText"), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar_empty;
	}

	public void visitWebsite(View v) {
		Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_url)));
		startActivity(urlIntent);
	}
}
