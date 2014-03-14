package com.artisan.android.demo.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsItem {

	private String id;

	private static final String DEFAULT_ICON_RES_NAME = "collections_view_as_list";
	private static final String DEFAULT_ICON_TEXT_RES_NAME= "list_item_link_text_default";

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class NewsItemValueContainer {
		@JsonProperty("author")
		private String author;

		@JsonProperty("body")
		private String contentText;

		@JsonProperty("created_at")
		private Date createdDate;

		@JsonProperty("provider")
		private String provider;

		@JsonProperty("profile_url")
		private String profilePicResName;

		@JsonProperty("url")
		private String linkUrl;
	}

	@JsonCreator
	public NewsItem(@JsonProperty("id") String id) {
		this.id = id;
	}


	@JsonProperty("value")
	private NewsItemValueContainer valueContainer;

	public String getId() {
		return id;
	}

	public String getContentText() {
		return valueContainer.contentText;
	}

	public String getIconTextResName() {
		return DEFAULT_ICON_TEXT_RES_NAME;
	}

	@JsonIgnore
	public String getIconResName() {
		return DEFAULT_ICON_RES_NAME;
	}

	public Date getCreatedDate() {
		return valueContainer.createdDate;
	}

	public String getProfilePicResName() {
		return valueContainer.profilePicResName;
	}

	public String getLinkUrl() {
		return valueContainer.linkUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsItem other = (NewsItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
