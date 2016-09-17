package com.dissertation.easycommuteguideonline.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dissertation.easycommuteguideonline.DataPackage.RequestPackage;

public class HttpManager {
	
	private static BufferedReader reader;
	private static double[] latlng;
	
	public static String getData (RequestPackage requestPackage) {

		String uri = requestPackage.getUri();
		
		if (requestPackage.getMethod().equals("GET")) {
			uri = uri + "?" + requestPackage.getEncodedParams();
		} 
		
		try {
			System.out.println(uri);
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestPackage.getMethod());
			
			if (requestPackage.getMethod().equals("POST")) {
				connection.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
				writer.write(requestPackage.getEncodedParams());
				writer.flush();
			}
			
			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			return sb.toString();
			
		} catch (IOException e) {
			System.out.println("The exception is: " + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				System.out.println("The exception is: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static double[] parseDestinationLocationDetails (String locationDetails) {
		
		latlng = new double[2];
			
		if (locationDetails != null && locationDetails.length() > 0) {
			System.out.println("Data extraction: ");
			
//			Extract the latitude and longitude, split by location
			String[] phase_1 = locationDetails.split("location");
			
//			Split by viewport
			String[] phase_2 = phase_1[1].split("viewport");
			
//			Split by lat
			String[] phase_3 = phase_2[0].split("\"lat\" : ");
			
//			Split by lat comma
			String[] phase_4 = phase_3[1].split(",");

			String latitude = phase_4[0].trim();
			
//			Split by lng semi-colon
			String[] phase_5 = phase_4[1].split(":");
			
//			Split by last curle bracket
			String[] phase_6 = phase_5[1].split(" ");
			
			/*for (String string : phase_6) {
				System.out.println("The partition is:\n" + string);
			}*/
			
			String phase_7[] = phase_6[1].trim().split(" ");
			
			String longitude = phase_7[0].trim();
			
			
/*		System.out.println("The latitude is: " + latitude);
			System.out.println("The longitude is: " + longitude);*/
			
			latlng[0] = Double.parseDouble(latitude);
			latlng[1] = Double.parseDouble(longitude);
		} else {
			System.out.println("The location details string was empty !");
		}
				
			return latlng;
		}

}
