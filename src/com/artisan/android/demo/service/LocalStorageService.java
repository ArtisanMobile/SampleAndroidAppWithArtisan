package com.artisan.android.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.artisan.android.demo.model.collection.NewsFeed;
import com.artisan.android.demo.model.collection.ShoppingCart;

public class LocalStorageService extends Service{

    private ShoppingCart sharedCart;
    private NewsFeed sharedNewsFeed;
    
    public class ServiceBinder extends Binder {
        public LocalStorageService getService() {
            return LocalStorageService.this;
        }
    };
    
    public void onCreate() {
        sharedCart = new ShoppingCart(this);
        sharedNewsFeed = new NewsFeed(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return new ServiceBinder();
    }
    
    public ShoppingCart getShoppingCart() {
        return sharedCart;
    }
    
    public NewsFeed getNewsFeed() {
        return sharedNewsFeed;
    }
    
}
