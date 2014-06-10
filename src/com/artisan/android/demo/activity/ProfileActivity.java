package com.artisan.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.artisan.android.demo.R;
import com.artisan.incodeapi.ArtisanLocationValue;
import com.artisan.incodeapi.ArtisanProfileManager;

public class ProfileActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		NumberPicker agePicker = (NumberPicker) findViewById(R.id.age_picker);
		agePicker.setMaxValue(122);
		agePicker.setMinValue(0);

		// setting a listener to update geocode text
		RadioGroup locationsGroup = (RadioGroup) findViewById(R.id.locations_group);
		locationsGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				TextView geocodeView = (TextView) findViewById(R.id.geocode_text);
				switch (checkedId) {
				case R.id.newyork_button:
					geocodeView.setText(getString(R.string.newyork_geo));
					break;
				case R.id.philly_button:
					geocodeView.setText(getString(R.string.philly_geo));
					break;
				case R.id.la_button:
					geocodeView.setText(getString(R.string.la_geo));
					break;
				case R.id.paris_button:
					geocodeView.setText(getString(R.string.paris_geo));
					break;
				}
			}
		});

		Button confirmButton = (Button) findViewById(R.id.confirm_profile_button);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateProfile();
				Toast.makeText(ProfileActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
				startActivity(intent);
			}

		});
	}

	private void updateProfile() {
		// ugly way of getting textview's updated geocode value and setting it as last known location
		String[] locs = ((TextView) findViewById(R.id.geocode_text)).getText().toString().split(",");
		ArtisanProfileManager.setLocationValue("lastKnownLocation", new ArtisanLocationValue(Double.parseDouble(locs[0]), Double.parseDouble(locs[1])));

		RadioGroup genderGroup = (RadioGroup) findViewById(R.id.gender_group);

		switch (genderGroup.getCheckedRadioButtonId()) {
		case R.id.male_button:
			ArtisanProfileManager.setGender(ArtisanProfileManager.Gender.Male);
			break;
		case R.id.female_button:
			ArtisanProfileManager.setGender(ArtisanProfileManager.Gender.Female);
			break;
		case R.id.unknown_button:
			ArtisanProfileManager.setGender(ArtisanProfileManager.Gender.NA);
			break;
		}

		ArtisanProfileManager.setUserAge(((NumberPicker) findViewById(R.id.age_picker)).getValue());

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar_empty;
	}

}
