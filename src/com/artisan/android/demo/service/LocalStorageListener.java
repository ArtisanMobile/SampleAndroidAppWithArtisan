package com.artisan.android.demo.service;

import com.artisan.android.demo.service.LocalStorageManager.LocalStorageException;

public interface LocalStorageListener<T> {
    public void onLoadComplete(T savedData);
    public void onError(LocalStorageException e);
}
