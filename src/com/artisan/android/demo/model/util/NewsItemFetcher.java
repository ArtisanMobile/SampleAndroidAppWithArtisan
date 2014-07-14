package com.artisan.android.demo.model.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.artisan.android.demo.model.NewsItem;
import com.artisan.android.demo.model.NewsItemResults;

public class NewsItemFetcher extends AsyncTask<Void, Void, String> {

	public static final String BROADCAST_NEWS_UPDATE = "NewsUpdateAvailable";
	private Context context;
	private List<NewsItem> newsItems;

	public NewsItemFetcher(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String jsonString = "";
		String urlString = "http://unifeed.heroku.com:80/api/artisan_demo/posts/?page=1&auth_token=c9f88b63ec2701f2acb49dfaa6c24c36&size=20";
		try {
			URL url = new URL(urlString);
			InputStream in = url.openStream();
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}
			jsonString = responseStrBuilder.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	@Override
	protected void onPostExecute(String result) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			NewsItemResults resultItems = mapper.readValue(result, new TypeReference<NewsItemResults>() {
				// nothing to do here, just a type reference
			});
			newsItems = resultItems.getItems();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(BROADCAST_NEWS_UPDATE);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	public List<NewsItem> getNewsItems() {
		return newsItems;
	}
}
