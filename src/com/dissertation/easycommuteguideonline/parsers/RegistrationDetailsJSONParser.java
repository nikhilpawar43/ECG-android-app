package com.dissertation.easycommuteguideonline.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.dissertation.easycommuteguideonline.model.BusRegistrationDetails;

public class RegistrationDetailsJSONParser {
	
	private static JSONObject jsonObject;
	private static BusRegistrationDetails busRegistrationDetails;
	
	public static BusRegistrationDetails parseFeed(String content) {
		
		try {
			
				
			jsonObject = new JSONObject(content);
			busRegistrationDetails = new BusRegistrationDetails();
			
			busRegistrationDetails.setBoardingPointName(jsonObject.getString("boardingPointName"));
			busRegistrationDetails.setMorningBusRouteno(jsonObject.getString("morningBusRouteno"));
			busRegistrationDetails.setMorningBusRoutename(jsonObject.getString("morningBusRoutename"));
			busRegistrationDetails.setEveningBusRouteno(jsonObject.getString("eveningBusRouteno"));
			busRegistrationDetails.setEveningBusRoutename(jsonObject.getString("eveningBusRoutename"));
			
			return busRegistrationDetails;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
