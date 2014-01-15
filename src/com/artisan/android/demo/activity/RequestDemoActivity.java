package com.artisan.android.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.artisan.android.demo.R;

public class RequestDemoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_demo);
	}

	public void contactArtisan(View view) {
		String subject = "";
		Editable nameText = ((EditText) findViewById(R.id.person_name)).getText();
		if (!TextUtils.isEmpty(nameText)) {
			subject = nameText + " ";
		}
		Editable companyNameText = ((EditText) findViewById(R.id.company_name)).getText();
		if (!TextUtils.isEmpty(companyNameText)) {
			if (subject.length() > 0) {
				subject += "at ";
			}
			subject += companyNameText + " ";
		}
		subject += "Interested in Artisan for Android";

		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:")); // only email apps should handle this
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "sales@useartisan.com" });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}

	}
}
