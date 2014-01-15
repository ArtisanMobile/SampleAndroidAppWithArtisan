package com.artisan.android.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

public class ArtisanDemoApplication extends Application {

	private static final String TAG = ArtisanDemoApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		// pre-seeding the newsfeed from JSON data
		copyAssetToInternalStorage("news_feed.json");
	}

	private void copyAssetToInternalStorage(String filename) {
		AssetManager assetManager = getAssets();
		try {
			File outFile = new File(this.getFilesDir(), filename);
			if (!outFile.exists()) {
				Log.d(TAG, String.format("Copying %s from assets to internal storage.", filename));
				InputStream in = assetManager.open(filename);
				OutputStream out = new FileOutputStream(outFile);
				copyFile(in, out);
				in.close();
				out.flush();
				out.close();
			} else {
				Log.d(TAG, String.format("File %s already exists in internal storage, did not copy.", filename));
			}
		} catch(IOException e) {
			Log.e(TAG, "Failed to copy asset file: " + filename, e);
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}