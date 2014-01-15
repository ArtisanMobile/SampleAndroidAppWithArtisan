package com.artisan.android.demo.model.collection;

import android.content.Context;

import com.artisan.android.demo.model.NewsItem;

public class NewsFeed extends ModelContainerSet<NewsItem> {
    private static final String FILENAME_DEFAULT = "news_feed.json";

    public NewsFeed(Context context, String filename) {
        super(NewsItem.class, context, filename);
    }

    public NewsFeed(Context context) {
        super(NewsItem.class, context, FILENAME_DEFAULT);
    }
}
