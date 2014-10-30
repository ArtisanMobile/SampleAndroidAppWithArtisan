package com.artisan.android.demo.model;

import java.text.NumberFormat;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class CartItem {

	private String id;
	private int pictureRes;
	private String titleShort;
	private String titleLong;
	private String description;
	private String category;
	private String subCategory;
	private String subSubCategory;
	private double price;

	@JsonCreator
	public CartItem(@JsonProperty("id") String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPictureRes() {
		return pictureRes;
	}

	public void setPictureRes(int pictureRes) {
		this.pictureRes = pictureRes;
	}

	public String getTitleShort() {
		return titleShort;
	}

	public void setTitleShort(String titleShort) {
		this.titleShort = titleShort;
	}

	public String getTitleLong() {
		return titleLong;
	}

	public void setTitleLong(String titleLong) {
		this.titleLong = titleLong;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@org.codehaus.jackson.annotate.JsonIgnore
	public String getPriceString() {
		return NumberFormat.getCurrencyInstance(Locale.US).format(price);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
		CartItem other = (CartItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getSubSubCategory() {
		return subSubCategory;
	}

	public void setSubSubCategory(String subSubCategory) {
		this.subSubCategory = subSubCategory;
	}
}
