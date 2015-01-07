package com.artisan.aspectj;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import android.app.Activity;
import android.os.Bundle;

import com.artisan.activity.ArtisanActivity;

@SuppressAjWarnings
public aspect ArtisanActivityInstrumentation {

	pointcut activityOnCreate(Activity activity, Bundle savedInstanceState): execution(* Activity+.onCreate(Bundle)) && target(activity) && args(savedInstanceState) && !target(ArtisanActivity+);

	after(Activity activity, Bundle savedInstanceState): activityOnCreate(activity, savedInstanceState) {
		ArtisanActivity.artisanOnCreate(activity);
	}

	pointcut activityOnStart(Activity activity): execution(* Activity+.onStart()) && target(activity) && !target(ArtisanActivity+);
	pointcut activityOnStartNoSuper(Activity activity): target(activity) && activityOnStart(Activity) && !cflowbelow(activityOnStart(Activity))  && !target(ArtisanActivity+);
	
	after(Activity activity): activityOnStartNoSuper(activity) {
		ArtisanActivity.artisanOnStart(activity);
	}

	pointcut activityOnStop(Activity activity): execution(* Activity+.onStop()) && target(activity)   && !target(ArtisanActivity+);
	pointcut activityOnStopNoSuper(Activity activity): target(activity) && activityOnStop(Activity) && !cflowbelow(activityOnStop(Activity))  && !target(ArtisanActivity+);
	
	after(Activity activity): activityOnStopNoSuper(activity) {
		ArtisanActivity.artisanOnStop(activity);
	}

	pointcut activityOnDestroy(Activity activity): execution(* Activity+.onDestroy(..)) && target(activity) && !target(ArtisanActivity+);

	before(Activity activity): activityOnDestroy(activity){
		ArtisanActivity.artisanOnDestroy();
	}
}
