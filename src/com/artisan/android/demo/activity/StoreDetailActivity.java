package com.artisan.android.demo.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artisan.android.demo.CustomArtisanService;
import com.artisan.android.demo.R;
import com.artisan.android.demo.model.CartItem;
import com.artisan.android.demo.model.collection.ShoppingCart;
import com.artisan.android.demo.service.LocalStorageListener;
import com.artisan.android.demo.service.LocalStorageManager.LocalStorageException;
import com.artisan.incodeapi.ArtisanExperimentManager;
import com.artisan.powerhooks.PowerHookManager;

public class StoreDetailActivity extends BaseActivity {

	public static final String EXTRA_STORE_ITEM = "storeItem";

	private static final String TAG = StoreDetailActivity.class.getSimpleName();

	private CartItem selectedItem;
	private ShoppingCart shoppingCart;

	private Bundle extras;

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_detail);

		ObjectMapper mapper = new ObjectMapper();

		extras = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

		try {
			selectedItem = mapper.readValue(extras.getByteArray(EXTRA_STORE_ITEM), CartItem.class);
			ImageView itemImage = (ImageView) findViewById(R.id.activity_store_detail_image);
			TextView itemTitle = (TextView) findViewById(R.id.activity_store_detail_title);
			TextView itemDescription = (TextView) findViewById(R.id.activity_store_detail_description);
			TextView itemPrice = (TextView) findViewById(R.id.activity_store_detail_price);

			itemImage.setImageResource(selectedItem.getPictureRes());
			itemTitle.setText((selectedItem.getTitleLong()));
			itemDescription.setText(selectedItem.getDescription());
			itemPrice.setText(selectedItem.getPrice());
		} catch (IOException e) {
			Log.e(TAG, "Error deserializing store item data", e);
		}
	}

	private void setBuyButtonResource(int newResourceId) {
		Button buyNowButton = (Button) findViewById(R.id.activity_store_detail_checkout);
		buyNowButton.setBackgroundResource(newResourceId);
	}

	// Hides the 'Add to Cart' button, which expands the 'Buy Now' button due to it's weight
	private void hideAddToCartButton() {
		View addToCartButton = findViewById(R.id.activity_store_detail_add_to_cart);
		// Check to make sure that we haven't already removed the button from the view
		if (addToCartButton != null) {
			((ViewManager)addToCartButton.getParent()).removeView(addToCartButton);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		storageManager.loadShoppingCart(cartListener);

		// If you get to this activity then you have seen the buy now experiment
		ArtisanExperimentManager.setExperimentViewedForExperiment(CustomArtisanService.BUY_NOW_EXPERIMENT);

		if (ArtisanExperimentManager.isCurrentVariantForExperiment(CustomArtisanService.CONTROL_VARIANT, CustomArtisanService.BUY_NOW_EXPERIMENT)) {
			Log.d(TAG, "Current Variant for Buy Now Experiment: Control");
			// Do nothing here as this is the control and users should experience this screen with no change
		} else if (ArtisanExperimentManager.isCurrentVariantForExperiment(CustomArtisanService.GREEN_VARIANT, CustomArtisanService.BUY_NOW_EXPERIMENT)) {
			Log.d(TAG, "Current Variant for Buy Now Experiment: Green Button");
			hideAddToCartButton();
			setBuyButtonResource(R.drawable.btn_long_green);
		} else if (ArtisanExperimentManager.isCurrentVariantForExperiment(CustomArtisanService.ORANGE_VARIANT, CustomArtisanService.BUY_NOW_EXPERIMENT)) {
			Log.d(TAG, "Current Variant for Buy Now Experiment: Orange Button");
			hideAddToCartButton();
			setBuyButtonResource(R.drawable.btn_long_orange);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putAll(extras);
		super.onSaveInstanceState(outState);
	}

	private LocalStorageListener<ShoppingCart> cartListener = new LocalStorageListener<ShoppingCart>() {
		public void onLoadComplete(ShoppingCart savedData) {
			shoppingCart = savedData;
		}
		public void onError(LocalStorageException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	};

	public boolean addSelectedItemToCart(View v) {
		boolean didAddToCart = false;
		if (shoppingCart == null) {
			Toast.makeText(this, "Could not add item to cart, please try again later.", Toast.LENGTH_LONG).show();
		} else if (selectedItem != null) {
			shoppingCart.addItem(selectedItem);
			// Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
			didAddToCart = true;
			Map<String, Object> extraData = new HashMap<String, Object>();
			extraData.put("context", this);
			PowerHookManager.executeBlock("showAlert", extraData);
		} else {
			Toast.makeText(this, "Could not add item to cart", Toast.LENGTH_SHORT).show();
		}
		return didAddToCart;
	}

	public void addSelectedItemToCartAndCheckout(View v) {
		// This is the click handler for the buy now button.
		// This is the goal of the Buy Now experiment--to get a click on the Buy Now button
		ArtisanExperimentManager.setTargetReachedForExperiment(CustomArtisanService.BUY_NOW_EXPERIMENT);
		boolean success = addSelectedItemToCart(v);
		if (success) {
			nextActivityIntent.setClass(this, CheckoutActivity.class);
			startActivity(nextActivityIntent);
		}
	}
}
