package com.artisan.android.demo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.artisan.activity.ArtisanActivity;
import com.artisan.android.demo.R;

public class WebViewActivity extends ArtisanActivity {

	public final static String WEB_VIEW_URL = "WEB_VIEW_URL";

	private WebView webView;
	private View progressBarContainter;

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Grab the url from the intent
		Intent intent = getIntent();
		String url = intent.getStringExtra(WEB_VIEW_URL);

		progressBarContainter = findViewById(R.id.progress_container);
		progressBarContainter.setVisibility(View.VISIBLE);

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new ArtisanWebViewClient());
		webView.loadUrl(url);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ArtisanWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					progressBarContainter.setVisibility(View.GONE);
				}
			}, 1000);
		}
	}
}
