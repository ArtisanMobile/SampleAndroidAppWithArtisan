package com.artisan.aspectj;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import android.app.Activity;
import android.app.ListActivity;
import android.app.TabActivity;
import android.view.InflateException;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;

import com.artisan.activity.ArtisanActivity;
import com.artisan.trackevent.TrackEvent_GB;
import com.artisan.trackevent.TrackEvent_ICS;

@SuppressWarnings("deprecation")
@SuppressAjWarnings
public aspect ArtisanEventHandler {

	pointcut onARClicked(View view) : execution(* *.*(View)) && args(view) && (within(Activity+ || View.OnClickListener+ || View.OnLongClickListener+)) && !within(ArtisanActivity+);;

	pointcut onARClickedNoSuper(View view) : args(view) && onARClicked(View) && !cflowbelow(onARClicked(View)) && !onARDrawerClosed(View)&& !onARDrawerOpened(View) && !onARPanelOpened(View) && !onARPanelClosed(View);

	before(View view)  : onARClickedNoSuper(view) {
		String methodName = thisJoinPoint.getStaticPart().getSignature().getName();
		if (view.isLongClickable()) {
			TrackEvent_ICS.onARViewLongClickedForICS(view, methodName);
		} else if (view.isClickable()) {
			TrackEvent_ICS.onARViewClickedForICS(view, methodName);
		}
	}

	pointcut onARItemClicked(AdapterView<?> adapterView, View view, int position, long id) : (execution(void AdapterView.OnItemClickListener+.*(AdapterView, View, int, long)) && args(adapterView, view, position, id)) || (execution(protected void onListItemClick(ListView,View,int,long)) && args(adapterView, view, position, id));

	pointcut onARItemClickedNoSuper(AdapterView<?> adapterView, View view, int position, long id) : args(adapterView, view, position, id) && onARItemClicked(AdapterView<?>, View, int , long) && !cflowbelow(onARItemClicked(AdapterView<?>, View, int , long));

	before(AdapterView<?> adapterView, View view, int position, long id) : onARItemClickedNoSuper(adapterView, view, position, id) {
		TrackEvent_ICS.onARItemClickedForICS(position, id, view, thisJoinPoint.getStaticPart().getSignature().getName());
	}

	pointcut onARItemLongClicked(AdapterView<?> adapterView, View view, int position, long id) : execution(* AdapterView.OnItemLongClickListener+.*(AdapterView, View, int, long)) && args(adapterView, view, position, id);

	before(AdapterView<?> adapterView, View view, int position, long id)  : 
		onARItemLongClicked(adapterView, view, position, id){
		TrackEvent_ICS.onARItemLongClickedForICS(position, id, view, thisJoinPoint.getStaticPart().getSignature().getName());
	}

	pointcut onARItemSelected(AdapterView<?> adapterView, View view, int position, long id)	: execution(* AdapterView.OnItemSelectedListener+.*(AdapterView, View, int, long)) && args(adapterView, view, position, id);

	before(AdapterView<?> adapterView, View view, int position, long id)  : 
			onARItemSelected(adapterView, view, position, id){
		// onItemSelected is called when the orientation is changed with the view parameter as null.In this case, we ignore the event.
		if (view != null) {
			TrackEvent_ICS.onARItemSelectedForICS(position, id, view);
		}
	}

	pointcut onARContextMenu(MenuItem menuItem) : execution(* *.onContextItemSelected(MenuItem)) && args(menuItem) && (within(Activity+));

	before(MenuItem menuItem) : onARContextMenu( menuItem){
		TrackEvent_GB.onARContextMenuItemSelected(menuItem);
	}

	pointcut onAROptionsItemSelected(MenuItem menuItem) : execution(* *.onOptionsItemSelected(MenuItem)) 
	&& args(menuItem) && (within(Activity+));

	pointcut onAROptionsItemSelectedNoSuper(MenuItem menuItem) : args(menuItem) && onAROptionsItemSelected(MenuItem)  && !cflowbelow(onAROptionsItemSelected(MenuItem));

	before(MenuItem menuItem) : onAROptionsItemSelectedNoSuper(menuItem){
		TrackEvent_GB.onARContextMenuItemSelected(menuItem);
	}

	pointcut onARStopTrackingTouch(SeekBar seekbar) : 
		execution(* *.onStopTrackingTouch(SeekBar)) && 
		args(seekbar) && (within(SeekBar.OnSeekBarChangeListener+));

	before(SeekBar seekbar) : onARStopTrackingTouch(seekbar){
		TrackEvent_GB.onARStopTrackingTouch(seekbar);
	}

	pointcut onARCheckedChangedCompoundButton(CompoundButton buttonView, boolean isChecked): 
		execution(* *.onCheckedChanged(CompoundButton, boolean)) && 
		args(buttonView, isChecked) && (within(CompoundButton.OnCheckedChangeListener+));

	before(CompoundButton buttonView, boolean isChecked) : onARCheckedChangedCompoundButton(buttonView, isChecked){
		TrackEvent_ICS.onARCheckedChangedCompoundButtonForICS(buttonView);
	}

	pointcut onARCheckedChangedRadioGroup(): 
		execution(* *.onCheckedChanged(..))  && (within(RadioGroup.OnCheckedChangeListener+));

	before() : onARCheckedChangedRadioGroup(){
		Object[] args = thisJoinPoint.getArgs();
		RadioGroup radioGroup = (RadioGroup) args[0];
		Integer id = (Integer) args[1];
		TrackEvent_GB.onARCheckedChangedRadioGroup(radioGroup, id.intValue());
	}

	pointcut onARDrawerClosed(View drawerView): execution(* *.onDrawerClosed(View)) && args(drawerView) && (within(android.support.v4.widget.DrawerLayout.DrawerListener+));

	before(View drawerView) : onARDrawerClosed(drawerView){
		TrackEvent_ICS.onARDrawerClosedForICS(drawerView);
	}

	pointcut onARDrawerOpened(View drawerView): execution(* *.onDrawerOpened(View)) && args(drawerView) && (within(android.support.v4.widget.DrawerLayout.DrawerListener+));

	before(View drawerView) : onARDrawerOpened(drawerView){
		TrackEvent_ICS.onARDrawerOpenedForICS(drawerView);
	}

	pointcut onARPanelOpened(View panel): execution(* *.onPanelOpened(View)) && args(panel) && (within(android.support.v4.widget.SlidingPaneLayout.PanelSlideListener+));

	before(View panel) : onARPanelOpened(panel){
		TrackEvent_GB.onARPanelOpened(panel);
	}

	pointcut onARPanelClosed(View panel): execution(* *.onPanelClosed(View)) && args(panel) && (within(android.support.v4.widget.SlidingPaneLayout.PanelSlideListener+));

	before(View panel) : onARPanelClosed(panel){
		TrackEvent_GB.onARPanelClosed(panel);
	}

	pointcut onARRatingChanged(RatingBar ratingBar, float rating, boolean fromUser): 
		execution(* *.onRatingChanged(RatingBar, float, boolean)) && 
		args(ratingBar, rating, fromUser) && (within(RatingBar.OnRatingBarChangeListener+));

	before(RatingBar ratingBar, float rating, boolean fromUser) : 
			onARRatingChanged(ratingBar, rating, fromUser){
		TrackEvent_GB.onARRatingChanged(ratingBar, rating);
	}

	pointcut activitySetContentViewInt(Integer layoutId, Activity activity): call(void Activity+.setContentView(int)) && args(layoutId) && target(activity) && !within(TabActivity+)  && !within(ArtisanActivity+);

	void around(Integer layoutId, Activity activity) : activitySetContentViewInt(layoutId, activity) {
		try {
			// NOTE: we don't proceed--instead we call setContentView(View) with our gesture overlay. This means the customer's custom implementaiton may not be called
			activity.setContentView(ArtisanActivity.artisanGetContentView(layoutId, activity)); // This will also be advised
		} catch (InflateException e) {
			proceed((Integer) layoutId, activity);
		}
	}

	pointcut activitySetContentView(View view, Activity activity): call(void Activity+.setContentView(View)) && args(view) && target(activity) && !within(TabActivity+)  && !within(ArtisanActivity+);

	void around(View view, Activity activity) : activitySetContentView(view, activity) {
		if (ArtisanActivity.alreadyHaveGestureOverlay(view)) {
			proceed(view, activity); // Proceed because overlay has been added.
		} else {
			// NOTE: we don't proceed--instead we call setContentView(View) with our gesture overlay. This means the customer's custom implementaiton may not be called
			View contentView = ArtisanActivity.artisanGetContentView(view, activity);
			activity.setContentView(contentView); // This will also be advised
		}
	}

	pointcut activitySetContentViewWithLayout(View view, LayoutParams params, Activity activity): call(void Activity+.setContentView(View, LayoutParams)) && args(view, params) && target(activity) && !within(TabActivity+) && !within(ArtisanActivity+);

	void around(View view, LayoutParams params, Activity activity) : activitySetContentViewWithLayout(view, params, activity) {
		if (ArtisanActivity.alreadyHaveGestureOverlay(view)) {
			proceed(view, params, activity); // Proceed because overlay has been added.
			return;
		}
		// NOTE: we don't proceed--instead we call setContentView(View) with our gesture overlay. This means the customer's custom implementaiton may not be called
		View contentView = ArtisanActivity.artisanGetContentView(view, params, activity);
		activity.setContentView(contentView);
	}

	/*
	 * For List Activities, make sure ArtisanGestureOverlayView is added if
	 * setContentView is never called We do this by advising onCreate : 1) Store
	 * index of list view in its parent view and remove it 2) Add the ListView
	 * to ArtisanGestureOverlayView 3) Put the Gesture Overlay in the same index
	 */
	after(): execution(* ListActivity+.onCreate(..)) {
		ListActivity listActivity = (ListActivity) thisJoinPoint.getTarget();
		ListView listView = (ListView) listActivity.getListView();
		ArtisanActivity.artisanOnListActivityCreate(listView, listActivity);
	}
}
