package com.artisan.android.demo.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsItemResults {

	@JsonProperty("rows")
	private List<NewsItem> items;

	public NewsItemResults() {
		// default constructor for Jackson
		items = new ArrayList<NewsItem>();
	}

	public List<NewsItem> getItems() {
		return items;
	}
}