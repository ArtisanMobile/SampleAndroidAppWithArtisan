package com.artisan.android.demo.activity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.ParseException;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.artisan.android.demo.ArtisanDemoApplication;
import com.artisan.android.demo.R;
import com.artisan.android.demo.model.CartItem;
import com.artisan.android.demo.model.collection.ShoppingCart;
import com.artisan.android.demo.service.LocalStorageListener;
import com.artisan.android.demo.service.LocalStorageManager.LocalStorageException;
import com.artisan.incodeapi.ArtisanProfileManager;
import com.artisan.incodeapi.ArtisanPurchaseWorkflowManager;
import com.artisan.incodeapi.ArtisanTrackingManager;
import com.artisan.powerhooks.PowerHookManager;

public class CheckoutActivity extends BaseActivity {

	private static final String TAG = CheckoutActivity.class.getSimpleName();

	private ListView checkoutList;
	private CheckoutAdapter checkoutAdapter;
	private TextView listPlaceholder;
	private ViewGroup checkoutTotalContainer;
	private TextView checkoutTotal;
	private Button checkoutSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_checkout);

		checkoutList = (ListView) findViewById(android.R.id.list);
		listPlaceholder = (TextView) findViewById(android.R.id.empty);
		checkoutTotalContainer = (ViewGroup) findViewById(R.id.activity_checkout_total_container);
		checkoutTotal = (TextView) findViewById(R.id.activity_checkout_total);
		checkoutSubmit = (Button) findViewById(R.id.activity_checkout_submit);

		listPlaceholder.setText(R.string.cart_loading);
		checkoutList.setEmptyView(listPlaceholder);

		checkoutAdapter = new CheckoutAdapter(new ArrayList<CartItem>());

		refreshCheckoutDisplay();
	}

	@Override
	public void onStart() {
		super.onStart();

		storageManager.loadShoppingCart(cartListener);

		Button checkoutButton = (Button) findViewById(R.id.activity_checkout_submit);
		checkoutButton.setText(PowerHookManager.getVariableValue("checkout_submit"));

		TextView checkoutTotalView = (TextView) findViewById(R.id.activity_checkout_total_label);
		checkoutTotalView.setText(PowerHookManager.getVariableValue("cart_total"));

	}

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar_empty;
	}

	private LocalStorageListener<ShoppingCart> cartListener = new LocalStorageListener<ShoppingCart>() {
		public void onLoadComplete(ShoppingCart savedData) {
			checkoutAdapter = new CheckoutAdapter(new ArrayList<CartItem>());

			shoppingCart = new ShoppingCart(CheckoutActivity.this);

			checkoutAdapter.addAll(shoppingCart.getItems());
			checkoutList.setAdapter(checkoutAdapter);
			listPlaceholder.setText(R.string.cart_empty);
			checkoutList.setEmptyView(listPlaceholder);

			refreshCheckoutDisplay();

		}

		public void onError(LocalStorageException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	};

	private class CheckoutAdapter extends ArrayAdapter<CartItem> {

		public CheckoutAdapter(List<CartItem> items) {
			super(CheckoutActivity.this, -1, -1, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(CheckoutActivity.this);
				convertView = inflater.inflate(R.layout.list_item_checkout, parent, false);
			}
			ImageView image = (ImageView) convertView.findViewById(R.id.list_item_image);
			TextView title = (TextView) convertView.findViewById(R.id.list_item_text);
			TextView subtitle = (TextView) convertView.findViewById(R.id.list_item_subtext);
			Button removeButton = (Button) convertView.findViewById(R.id.list_item_remove);

			CartItem cartItem = getItem(position);

			image.setImageResource(cartItem.getPictureRes());

			title.setText(cartItem.getTitleLong());
			subtitle.setText(cartItem.getPriceString());
			removeButton.setTag(R.id.tag_list_item_position, position);
			removeButton.setOnClickListener(removeButtonListener);
			removeButton.setText(PowerHookManager.getVariableValue("cart_item_remove"));
			return convertView;
		}

		private View.OnClickListener removeButtonListener = new View.OnClickListener() {

			public void onClick(View v) {
				int clickedPosition = (Integer) v.getTag(R.id.tag_list_item_position);
				CartItem itemToRemove = getItem(clickedPosition);
				checkoutAdapter.remove(itemToRemove);
				shoppingCart.removeItem(itemToRemove);
				checkoutAdapter.notifyDataSetChanged();

				Toast.makeText(CheckoutActivity.this, "Item removed from cart.", Toast.LENGTH_SHORT).show();

				refreshCheckoutDisplay();
			}
		};
	}

	private void refreshCheckoutDisplay() {
		if (checkoutAdapter.isEmpty()) {
			checkoutTotalContainer.setVisibility(View.GONE);
			checkoutSubmit.setVisibility(View.GONE);
		} else {
			checkoutTotalContainer.setVisibility(View.VISIBLE);
			checkoutSubmit.setVisibility(View.VISIBLE);
			checkoutTotal.setText(calculatePrice());
		}
	}

	private String calculatePrice() {
		BigDecimal total = new BigDecimal(0);
		for (int i = 0; i < checkoutAdapter.getCount(); i++) {
			CartItem cartItem = checkoutAdapter.getItem(i);
			BigDecimal price = new BigDecimal("0");
			try {
				price = new BigDecimal(cartItem.getPrice());
			} catch (ParseException e) {
				Log.e(TAG, e.getMessage(), e);
			}
			total = total.add(price);
		}
		ArtisanProfileManager.setNumberValue("orderTotal", total);
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
		return currencyFormatter.format(total);
	}

	public void onCheckout(View v) {
		ArtisanPurchaseWorkflowManager.cartCheckoutSucceededWithShippingAndTax(0, 0);

		shoppingCart.removeAll();
		checkoutAdapter.clear();

		// refresh detail screens
		StoreDetailActivity.finishAll();

		// CUSTOM ANALYTICS EVENT
		// Here is an example of using the Artisan Tracking Manager to track a custom analytics event with extra data
		Map<String, String> cartInfo = new HashMap<String, String>();
		cartInfo.put("cart total", checkoutTotal.getText().toString());
		cartInfo.put("item count", "" + shoppingCart.getItems().size());
		ArtisanTrackingManager.trackEvent("checked out", cartInfo);

		// update a user profile variable -- this will get sent up to Artisan automatically
		ArtisanProfileManager.setNumberValue("totalOrderCount", ArtisanDemoApplication.totalOrderCount++);

		Toast.makeText(this, PowerHookManager.getVariableValue("purchase_thanks"), Toast.LENGTH_LONG).show();
		nextActivityIntent.setClass(this, StoreActivity.class);
		startActivity(nextActivityIntent);
	}
}
