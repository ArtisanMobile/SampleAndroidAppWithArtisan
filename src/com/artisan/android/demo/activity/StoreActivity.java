package com.artisan.android.demo.activity;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.artisan.android.demo.ArtisanDemoApplication;
import com.artisan.android.demo.R;
import com.artisan.android.demo.model.CartItem;
import com.artisan.android.demo.model.collection.ShoppingCart;
import com.artisan.android.demo.service.LocalStorageListener;
import com.artisan.android.demo.service.LocalStorageManager.LocalStorageException;
import com.artisan.incodeapi.ArtisanExperimentManager;

public class StoreActivity extends BaseActivity {

	GridLayout imageGrid;
	private ShoppingCart shoppingCart;

	private static final String TAG = StoreActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		imageGrid = (GridLayout) findViewById(R.id.activity_store_grid);

		for (int i = 0; i < imageGrid.getChildCount(); i++) {
			View tile = imageGrid.getChildAt(i);
			tile.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					navigateToDetail(v);
				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		storageManager.loadShoppingCart(cartListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		// load shopping cart on resume to reflect changes after backpress from detail page, or hitting the checkout button
		storageManager.loadShoppingCart(cartListener);
		// if you get to this activity then you have viewed the skip detail experiment
		ArtisanExperimentManager.setExperimentViewedForExperiment(ArtisanDemoApplication.SKIP_DETAIL_EXPERIMENT);
	}

	private LocalStorageListener<ShoppingCart> cartListener = new LocalStorageListener<ShoppingCart>() {
		public void onLoadComplete(ShoppingCart savedData) {
			shoppingCart = savedData;
			updateOptionsMenu(shoppingCart.getItems().size());
		}

		public void onError(LocalStorageException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	};

	private void navigateToDetail(View clickedView) {
		int clickedIndex = imageGrid.indexOfChild(clickedView);

		String[] itemTitles = getResources().getStringArray(R.array.store_item_titles);
		String[] itemLongTitles = getResources().getStringArray(R.array.store_item_titles_long);
		TypedArray itemDrawables = getResources().obtainTypedArray(R.array.store_detail_drawables);

		int detailDrawable = itemDrawables.getResourceId(clickedIndex, R.drawable.shirt1);
		String detailTitle = itemTitles[clickedIndex];
		String detailTitleLong = itemLongTitles[clickedIndex];
		String detailDescription = getString(R.string.ipsum);
		String detailPrice = "$10.00";

		CartItem selectedItem = new CartItem(Integer.toString(clickedIndex));
		selectedItem.setPictureRes(detailDrawable);
		selectedItem.setTitleShort(detailTitle);
		selectedItem.setTitleLong(detailTitleLong);
		selectedItem.setDescription(detailDescription);
		selectedItem.setPrice(detailPrice);

		itemDrawables.recycle();

		if (ArtisanExperimentManager.isCurrentVariantForExperiment(ArtisanDemoApplication.CONTROL_VARIANT, ArtisanDemoApplication.SKIP_DETAIL_EXPERIMENT)) {
			ArtisanExperimentManager.setTargetReachedForExperiment(ArtisanDemoApplication.SKIP_DETAIL_EXPERIMENT);
			nextActivityIntent.setClass(this, StoreDetailActivity.class);
			ObjectMapper mapper = new ObjectMapper();
			try {
				byte[] serializedItem = mapper.writeValueAsBytes(selectedItem);

				nextActivityIntent.putExtra(StoreDetailActivity.EXTRA_STORE_ITEM, serializedItem);
			} catch (IOException e) {
				Log.e(TAG, "Error serializing store item data", e);
			}
		} else if (ArtisanExperimentManager.isCurrentVariantForExperiment(ArtisanDemoApplication.SKIP_DETAIL_VARIANT, ArtisanDemoApplication.SKIP_DETAIL_EXPERIMENT)) {
			ArtisanExperimentManager.setTargetReachedForExperiment(ArtisanDemoApplication.SKIP_DETAIL_EXPERIMENT);
			nextActivityIntent.setClass(this, CheckoutActivity.class);
			if (shoppingCart != null) {
				shoppingCart.addItem(selectedItem);
			} else {
				Toast.makeText(this, "Could not add item to cart, please try again later.", Toast.LENGTH_LONG).show();
			}
		}
		startActivity(nextActivityIntent);
	}
}
