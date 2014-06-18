package com.artisan.android.demo.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class JsonRequester<JacksonAnnotatedType> extends AsyncTask<String, String, String> {

	private String jsonString;

	public JsonRequester(Context context, Class<JacksonAnnotatedType> modelClass) {

	}

	@Override
	protected String doInBackground(String... params) {
		System.out.println("doing bg");
		String urlString = "http://unifeed.heroku.com:80/api/artisan_demo/posts/?page=1&auth_token=c9f88b63ec2701f2acb49dfaa6c24c36&size=20";
		URL url;
		try {
			url = new URL(urlString);
			InputStream in = url.openStream();
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			jsonString = (new JSONObject(responseStrBuilder.toString())).toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonString;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

}
