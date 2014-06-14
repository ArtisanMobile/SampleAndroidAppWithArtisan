package com.artisan.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artisan.activity.ArtisanActivity;
import com.artisan.android.demo.R;
import com.artisan.android.demo.model.collection.ShoppingCart;
import com.artisan.android.demo.service.LocalStorageListener;
import com.artisan.android.demo.service.LocalStorageManager;
import com.artisan.android.demo.service.LocalStorageManager.LocalStorageException;

public class BaseActivity extends ArtisanActivity {

	private static final String TAG = BaseActivity.class.getSimpleName();

	protected final LocalStorageManager storageManager = new LocalStorageManager();
	protected final Intent nextActivityIntent = new Intent();
	protected Menu optionsMenu;
	protected static ShoppingCart shoppingCart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nextActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	}

	@Override
	protected void onStart() {
		super.onStart();
		storageManager.start(this);
		storageManager.loadShoppingCart(cartListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		storageManager.stop(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_bar_settings:
			nextActivityIntent.setClass(this, ProfileActivity.class);
			startActivity(nextActivityIntent);
			return true;
		default:
			return false;
		}
	}

	protected int getOptionsMenuResource() {
		return R.menu.action_bar;
	}

	protected LocalStorageListener<ShoppingCart> cartListener = new LocalStorageListener<ShoppingCart>() {
		public void onLoadComplete(ShoppingCart savedData) {
			shoppingCart = new ShoppingCart(BaseActivity.this);
			updateOptionsMenu(shoppingCart.getItems().size());
		}

		public void onError(LocalStorageException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	};

	protected void updateOptionsMenu(int cartItems) {
		// set click listener for layout because isn't a menu item, nor can you access the menu item's click listener
		try {
			RelativeLayout badgeLayout = (RelativeLayout) optionsMenu.findItem(R.id.action_bar_checkout).getActionView();
			optionsMenu.getItem(1).getActionView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					nextActivityIntent.setClass(BaseActivity.this, CheckoutActivity.class);
					startActivity(nextActivityIntent);
				}

			});
			TextView itemView = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
			itemView.setText(String.valueOf(cartItems));
			itemView.setVisibility(View.VISIBLE);

			if (cartItems == 0) {
				itemView.setVisibility(View.GONE);
			}
		} catch (NullPointerException e) {
			// optionsMenu is null because onCreateOptionsMenu doesn't get called
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		optionsMenu = menu;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(getOptionsMenuResource(), menu);

		// create dynamic cart icon with # of items in it only when cart is displayed in the first place

		if (getOptionsMenuResource() == (R.menu.action_bar)) {
			// load shopping cart; this will then update the menu to properly display the number of items
			storageManager.loadShoppingCart(cartListener);
		}
		return true;
	}

}
