package com.artisan.android.demo.model.collection;


import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.artisan.android.demo.model.NewsItem;
import com.artisan.android.demo.model.util.NewsItemFetchTask;

public class NewsFeed extends BroadcastReceiver {

	private List<NewsItem> modelSet = new ArrayList<NewsItem>();
	private NewsItemFetchTask fetchTask;

	public NewsFeed(Context context) {
		// The news feed is down for a long dirt nap.

		// modelSet = new ArrayList<NewsItem>();
		// fetchTask = new NewsItemFetchTask(context);
		// fetchTask.execute();
		// IntentFilter newsEvents = new IntentFilter();
		// newsEvents.addAction(NewsItemFetchTask.BROADCAST_NEWS_UPDATE);
		// LocalBroadcastManager.getInstance(context).registerReceiver(this, newsEvents);
	}

	public void setModelSet(List<NewsItem> modelSet) {
		this.modelSet = modelSet;
	}

	public List<NewsItem> getItems() {
		return new ArrayList<NewsItem>(modelSet);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (NewsItemFetchTask.BROADCAST_NEWS_UPDATE.equals(intent.getAction())) {
			this.modelSet = fetchTask.getNewsItems();
		}
	}
}