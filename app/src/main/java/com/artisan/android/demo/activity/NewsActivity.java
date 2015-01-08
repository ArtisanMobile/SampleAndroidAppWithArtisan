package com.artisan.android.demo.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.artisan.android.demo.R;
import com.artisan.android.demo.model.NewsItem;
import com.artisan.android.demo.model.collection.NewsFeed;
import com.artisan.android.demo.service.LocalStorageListener;
import com.artisan.android.demo.service.LocalStorageManager.LocalStorageException;
import com.artisan.incodeapi.ArtisanTrackingManager;

public class NewsActivity extends BaseActivity {
	private static final String TAG = NewsActivity.class.getSimpleName();

	private ListView newsList;
	private NewsFeed newsFeed;
	private NewsFeedAdapter newsAdapter;
	private TextView listPlaceholder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_news);

		listPlaceholder = (TextView) findViewById(R.id.empty_news);

		listPlaceholder.setText(R.string.news_empty);
		newsList = (ListView) findViewById(R.id.news_list);
		newsList.setEmptyView(listPlaceholder);
	}

	@Override
	public void onStart() {
		super.onStart();
		storageManager.loadNewsFeed(newsFeedListener);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected int getOptionsMenuResource() {
		return R.menu.action_bar_empty;
	}

	private LocalStorageListener<NewsFeed> newsFeedListener = new LocalStorageListener<NewsFeed>() {
		public void onLoadComplete(NewsFeed savedData) {
			newsFeed = savedData;
			newsAdapter = new NewsFeedAdapter(newsFeed.getItems());
			newsList.setAdapter(newsAdapter);
			listPlaceholder.setText(R.string.news_empty);
			newsList.setEmptyView(listPlaceholder);
		}

		public void onError(LocalStorageException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	};

	private class NewsFeedAdapter extends ArrayAdapter<NewsItem> {
		public NewsFeedAdapter(List<NewsItem> items) {
			super(NewsActivity.this, -1, -1, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(NewsActivity.this);
				convertView = inflater.inflate(R.layout.list_item_news, parent, false);
			}

			TextView contentText = (TextView) convertView.findViewById(R.id.list_item_text);
			TextView dateText = (TextView) convertView.findViewById(R.id.list_item_date);
			TextView actionButton = (TextView) convertView.findViewById(R.id.list_item_action);
			ImageView image = (ImageView) convertView.findViewById(R.id.list_item_image);

			final NewsItem newsItem = getItem(position);

			Resources r = getResources();
			int iconRes = r.getIdentifier(newsItem.getIconResName(), "drawable", getContext().getPackageName());
			int iconTextRes = r.getIdentifier(newsItem.getIconTextResName(), "string", getContext().getPackageName());

			// hardcoded string because unifeed doesn't provide pic link
			int imageRes = r.getIdentifier("artisan_profile_pic", "drawable", getContext().getPackageName());

			Date date = new Date(newsItem.getCreatedDate() * 1000);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
			String dateString = dateFormat.format(date);

			contentText.setText(newsItem.getContentText());
			dateText.setText(dateString);
			image.setImageResource(imageRes);
			actionButton.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0);
			actionButton.setText(iconTextRes);
			actionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					showLinkClicked(newsItem.getLinkUrl());
				}
			});

			return convertView;
		}

		private void showLinkClicked(String linkUrl) {
			// CUSTOM ARTISAN ANALYTICS EVENT
			Map<String, String> details = new HashMap<String, String>();
			details.put("url", linkUrl);
			ArtisanTrackingManager.trackEvent("news feed link clicked", details, "interaction", "visit website", "clicked");

			Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
			startActivity(urlIntent);
		}
	}
}
