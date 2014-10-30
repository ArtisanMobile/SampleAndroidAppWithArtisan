package com.artisan.android.demo.model.collection;

import java.util.Currency;
import java.util.Locale;

import android.content.Context;

import com.artisan.android.demo.model.CartItem;
import com.artisan.incodeapi.ArtisanPurchaseWorkflowManager;

public class ShoppingCart extends ModelContainerSet<CartItem> {

	private static final String FILENAME_DEFAULT = "shopping_cart.json";

	public ShoppingCart(Context context, String filename) {
		super(CartItem.class, context, filename);
	}

	public ShoppingCart(Context context) {
		super(CartItem.class, context, FILENAME_DEFAULT);
	}

	@Override
	public boolean addItem(CartItem item) {
		boolean added = super.addItem(item);
		if (added) {
			ArtisanPurchaseWorkflowManager.addItemToCart(item.getId(), item.getPrice(), Currency.getInstance(Locale.US), item.getDescription(), item.getCategory(), item.getSubCategory(), item.getSubSubCategory(), 1, null);
		}
		return added;
	}

	@Override
	public boolean removeItem(CartItem item) {
		boolean removed = super.removeItem(item);
		if (removed) {
			ArtisanPurchaseWorkflowManager.removeItemFromCart(item.getId(), item.getPrice(), item.getDescription(), 1);
		}
		return removed;
	}

	public void removeAll() {
		for (CartItem item : getItems()) {
			super.removeItem(item);
		}
	}
}
