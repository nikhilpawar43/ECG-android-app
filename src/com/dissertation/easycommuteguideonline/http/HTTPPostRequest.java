package com.dissertation.easycommuteguideonline.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HTTPPostRequest {
	
	private HttpURLConnection connection = null;
	private StringBuilder placeJSONResults = new StringBuilder();
	
	private static final String LOG_TAG = "Google Places Autocomplete";
	
	public void getPlaceDetailsJSONHttpResponse (String baseURL) {
		
		try {
			StringBuilder sb = new StringBuilder(baseURL);

			URL url = new URL(sb.toString());
			
			System.out.println("URL: "+url);
			connection = (HttpURLConnection) url.openConnection();
			
			System.out.println("The connection is: " + connection.toString());
			
			InputStream is = null;
			try {
				is =  connection.getInputStream();
				System.out.println("The input stream is: " + is.toString());
			} catch (Exception e) {
				Log.e(LOG_TAG, "The exception with input stream is: " + e.getMessage(), e);
			}
			
			InputStreamReader incon = new InputStreamReader(is);

			// Load the results into a StringBuilder
						int read;
			char[] buff = new char[1024];
			while ((read = incon.read(buff)) != -1) {
				placeJSONResults.append(buff, 0, read);
			}
						
			System.out.println("Connection successful.");
			
			
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL: " + e.getMessage(), e);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API: " + e.getMessage(), e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public void getPlacesJSONHttpResponse (String baseURL) {
		
		
		
	}

}
