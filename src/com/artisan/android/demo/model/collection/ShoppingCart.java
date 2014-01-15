package com.artisan.android.demo.model.collection;

import android.content.Context;

import com.artisan.android.demo.model.CartItem;


public class ShoppingCart extends ModelContainerSet<CartItem>{

    private static final String FILENAME_DEFAULT = "shopping_cart.json";

    public ShoppingCart(Context context,
            String filename) {
        super(CartItem.class, context, filename);
        // TODO Auto-generated constructor stub
    }

    public ShoppingCart(Context context) {
        super(CartItem.class, context, FILENAME_DEFAULT);
    }
}