package com.artisan.android.demo.model.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JsonSerializer<JacksonAnnotatedType> {

	private static final String TAG = JsonSerializer.class.getSimpleName();

	private String filePath;
	private Context appContext;
	private Class<JacksonAnnotatedType> annotatedClass;

	public JsonSerializer(Context c, String filePath, Class<JacksonAnnotatedType> annotatedClass) {
		this.filePath = filePath;
		appContext = c.getApplicationContext();
		this.annotatedClass = annotatedClass;
	}

	public LinkedHashSet<JacksonAnnotatedType> loadFromJson() {
		File jsonFile = new File(appContext.getFilesDir(), filePath);
		ObjectMapper mapper = new ObjectMapper();

		if (jsonFile.exists()) {
			try {
				JavaType containerType = mapper.getTypeFactory().constructCollectionType(LinkedHashSet.class, annotatedClass);
				LinkedHashSet<JacksonAnnotatedType> deserializedObject = mapper.readValue(jsonFile, containerType);
				return deserializedObject;
			} catch (IOException e) {
				String message = String.format("Error loading JSON file '%s' from disk", filePath);
				Log.e(TAG, message, e);
			}
		} else {
			String message = String.format("No JSON data loaded, '%s' does not yet exist.", filePath);
			Log.d(TAG, message);
		}
		return new LinkedHashSet<JacksonAnnotatedType>();
	}

	public boolean saveAsJson(LinkedHashSet<JacksonAnnotatedType> saveData) {
		File jsonFile = new File(appContext.getFilesDir(), filePath);
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(jsonFile, saveData);
			return true;
		} catch (IOException e) {
			String message = String.format("Error writing JSON file '%s' to disk", filePath);
			Log.e(TAG, message, e);
		}
		return false;
	}

	public boolean saveJson(JSONObject saveData) {
		File jsonFile = new File(appContext.getFilesDir(), filePath);
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(jsonFile, saveData);
			return true;
		} catch (IOException e) {
			String message = String.format("Error writing JSON file '%s' to disk", filePath);
			Log.e(TAG, message, e);
		}
		return false;
	}
}
