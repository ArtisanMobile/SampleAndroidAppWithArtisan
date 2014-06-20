package com.artisan.android.demo.model.collection;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.artisan.android.demo.model.NewsItem;
import com.artisan.android.demo.model.util.JsonRequester;

public class NewsFeed {

	private LinkedHashSet<NewsItem> modelSet;
	private JsonRequester<NewsItem> jsonRequester;

	public NewsFeed(Context context) {

		String jsonString = "";

		jsonRequester = new JsonRequester(context, NewsItem.class);
		jsonRequester.execute();
		modelSet = new LinkedHashSet<NewsItem>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonString = jsonRequester.get();
			JSONObject json = new JSONObject(jsonString);
			Iterator<?> keys = json.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (key.equals("rows")) {
					JSONArray rows = json.getJSONArray("rows");
					for (int i = 0; i < rows.length(); i++) {
						JSONObject tweet = rows.getJSONObject(i);
						System.out.println(tweet.toString());
						NewsItem news = mapper.readValue(new ByteArrayInputStream(tweet.toString().getBytes("UTF-8")), NewsItem.class);
						modelSet.add(news);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<NewsItem> getItems() {
		return new ArrayList<NewsItem>(modelSet);
	}

}
