package com.artisan.android.demo.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.artisan.android.demo.R;
import com.artisan.incodeapi.ArtisanLocationValue;
import com.artisan.incodeapi.ArtisanProfileManager;
import com.artisan.incodeapi.ArtisanTrackingManager;

public class ProfileActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		// setting values for number picker
		NumberPicker agePicker = (NumberPicker) findViewById(R.id.age_picker);
		agePicker.setMaxValue(122);
		agePicker.setMinValue(0);
		agePicker.setValue(30);

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

		TextView ageAndGenderLabel = (TextView) findViewById(R.id.age_and_gender_label);

		// setting age and gender to be selected by default
		ageAndGenderLabel.setSelected(true);

		ageAndGenderLabel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// set other subviews invisible, and set age picker visible
				LinearLayout ageAndGenderView = (LinearLayout) findViewById(R.id.age_and_gender_view);
				ageAndGenderView.setVisibility(View.VISIBLE);

				LinearLayout locationsView = (LinearLayout) findViewById(R.id.locations_view);
				locationsView.setVisibility(View.GONE);

				TextView ageAndGenderLabel = (TextView) findViewById(R.id.age_and_gender_label);
				ageAndGenderLabel.setSelected(true);

				TextView locationsLabel = (TextView) findViewById(R.id.location_label);
				locationsLabel.setSelected(false);

			}

		});

		TextView locationsLabel = (TextView) findViewById(R.id.location_label);
		locationsLabel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// set other subviews invisible, and set age picker visible
				LinearLayout ageAndGenderView = (LinearLayout) findViewById(R.id.age_and_gender_view);
				ageAndGenderView.setVisibility(View.GONE);

				LinearLayout locationsView = (LinearLayout) findViewById(R.id.locations_view);
				locationsView.setVisibility(View.VISIBLE);

				TextView ageAndGenderLabel = (TextView) findViewById(R.id.age_and_gender_label);
				ageAndGenderLabel.setSelected(false);

				TextView locationsLabel = (TextView) findViewById(R.id.location_label);
				locationsLabel.setSelected(true);
			}

		});

		Button confirmButton = (Button) findViewById(R.id.confirm_profile_button);
		confirmButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				saveChanges();
				updateProfile();
				Toast.makeText(ProfileActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
				nextActivityIntent.setClass(ProfileActivity.this, HomeActivity.class);
				startActivity(nextActivityIntent);
			}

		});

	}

	private void loadValues() {
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		int age = prefs.getInt("age", -1);
		int gender = prefs.getInt("gender", 0);
		int loc = prefs.getInt("loc", 0);

		// if non-default
		if (gender != 0) {
			((RadioGroup) findViewById(R.id.gender_group)).check(gender);
		}

		if (loc != 0) {
			((RadioGroup) findViewById(R.id.locations_group)).check(loc);
		}

		if (age >= 0) {
			((NumberPicker) findViewById(R.id.age_picker)).setValue(age);
		}

	}

	private void saveChanges() {
		SharedPreferences.Editor prefs = getPreferences(Context.MODE_PRIVATE).edit();

		int age = ((NumberPicker) findViewById(R.id.age_picker)).getValue();
		int gender = ((RadioGroup) findViewById(R.id.gender_group)).getCheckedRadioButtonId();
		int loc = ((RadioGroup) findViewById(R.id.locations_group)).getCheckedRadioButtonId();

		prefs.putInt("age", age);
		prefs.putInt("gender", gender);
		prefs.putInt("loc", loc);

		prefs.commit();

		// TRACKING CUSTOM ARTISAN EVENT
		Map<String, String> details = new HashMap<String, String>();
		details.put("age", "" + age);
		details.put("gender", "" + gender);
		details.put("loc", "" + loc);
		ArtisanTrackingManager.trackEvent("Artisan user profile updated", details, "profile updates", "custom", null);
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
		loadValues();
	}

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar_empty;
	}

}
