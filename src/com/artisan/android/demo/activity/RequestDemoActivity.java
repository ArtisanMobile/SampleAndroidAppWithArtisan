package com.artisan.android.demo.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.artisan.android.demo.R;
import com.artisan.hybrid.ArtisanJavascriptBridge;
import com.artisan.incodeapi.ArtisanProfileManager;
import com.artisan.incodeapi.ArtisanTrackingManager;

public class RequestDemoActivity extends BaseActivity {

	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_demo);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		String url = "file:///android_asset/requestdemo.html";

		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		ArtisanJavascriptBridge.addArtisanJavascriptInterfaceToWebView(this, webView);
		webView.addJavascriptInterface(new ContactInterface(this), "ContactArtisan");
		webView.loadUrl(url);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public class ContactInterface {

		Context context;

		ContactInterface(Context c) {
			context = c;
		}

		@JavascriptInterface
		public void contactArtisan(String nameText, String companyNameText) {
			Map<String, String> contactInfo = new HashMap<String, String>();

			String subject = "";

			// TODO check if textutils works on string obj instead of
			// editabletext
			if (!nameText.equals(null)) {
				subject = nameText + " ";
				contactInfo.put("name", nameText.toString());
			}

			if (!companyNameText.equals(null)) {
				if (subject.length() > 0) {
					subject += "at ";
				}
				subject += companyNameText + " ";
				contactInfo.put("company", companyNameText.toString());
			}
			subject += "Interested in Artisan for Android";

			// CUSTOM ANALYTICS EVENT
			ArtisanTrackingManager.trackEvent("contactArtisan pressed", contactInfo);

			// Update user profile variable
			ArtisanProfileManager.setStringValue("visitorType", "contacted");

			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("mailto:")); // only email apps should
													// handle this
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "sales@useartisan.com" });
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			}
		}

	}

}
