package com.artisan.android.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.artisan.android.demo.R;
import com.artisan.incodeapi.ArtisanTrackingManager;
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

		// API EXAMPLE: Showing a toast message when you enter this screen with a Power Hook value that is configurable from Artisan Tools
		Toast.makeText(this, PowerHookManager.getVariableValue("welcome_text"), Toast.LENGTH_SHORT).show();

		Button visitWebsiteButton = (Button) this.findViewById(R.id.activity_about_visit_website);
		// API EXAMPLE: Setting the text on the button with the text from a Power Hook. This value is configurable from Artisan Tools.
		visitWebsiteButton.setText(PowerHookManager.getVariableValue("visit_website"));
	}

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar_empty;
	}

	public void visitWebsite(View v) {
		ArtisanTrackingManager.trackEvent("Visit Website Clicked");
		Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PowerHookManager.getVariableValue("website_url")));
		startActivity(urlIntent);
	}
}
