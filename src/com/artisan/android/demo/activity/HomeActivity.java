package com.artisan.android.demo.activity;

import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

import com.artisan.android.demo.R;
import com.artisan.incodeapi.ArtisanExperimentManager;
import com.artisan.incodeapi.ExperimentDetails;
import com.artisan.manager.ArtisanManager;
import com.artisan.manager.ArtisanManagerCallback;
import com.artisan.powerhooks.PowerHookManager;

public class HomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ArtisanManager.onFirstPlaylistDownloaded(this, new ArtisanManagerCallback() {

			public void execute() {
				reorderHomePageBasedOnPowerHook();

				List<ExperimentDetails> experimentDetails = ArtisanExperimentManager.getCurrentExperimentDetails();
				for (ExperimentDetails experiment : experimentDetails) {
					// unique identifier for this experiment (alphanumeric)
					String experimentID = experiment.getExperimentId();

					// human-friendly name for this experiment, as you see in Artisan Tools
					String experimentName = experiment.getExperimentName();

					// unique identifier for this variation (alphanumeric)
					String currentVariationId = experiment.getCurrentVariationId();

					// human-friendly name for this variation, as you see in Artisan Tools
					String currentVariationName = experiment.getCurrentVariationName();

					Log.d("Just Testing", "Experiment: " + experimentName + " (" + experimentID + ") variation: " + currentVariationName + " (" + currentVariationId + ")");
				}
				Set<String> currentVariationIds = ArtisanExperimentManager.getCurrentVariationIds();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		reorderHomePageBasedOnPowerHook();
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

	private void reorderHomePageBasedOnPowerHook() {
		// Reorder the home page based on an Artisan Power Hook
		View otherNavigation = findViewById(R.id.otherNavigation);
		View aboutNavigation = findViewById(R.id.aboutNavigation);
		View storeNavigation = findViewById(R.id.storeNavigation);

		GridLayout parent = (GridLayout) storeNavigation.getParent();

		// about|store|other
		String homePageOrdering = PowerHookManager.getVariableValue("home_page_ordering");
		String[] order = homePageOrdering.split("\\|");
		for (String string : order) {
			if ("store".equals(string)) {
				parent.removeView(storeNavigation);
				parent.addView(storeNavigation);
			} else if ("other".equals(string)) {
				parent.removeView(otherNavigation);
				parent.addView(otherNavigation);
			} else if ("about".equals(string)) {
				parent.removeView(aboutNavigation);
				parent.addView(aboutNavigation);
			}
		}
	}
}
