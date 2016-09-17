package com.dissertation.easycommuteguideonline.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dissertation.easycommuteguideonline.model.BusRoute;
import com.dissertation.easycommuteguideonline.model.SearchResults;

public class BusSearchJSONParser {
	
	private static JSONArray jsonArray;
	private static JSONObject jsonObject1, jsonObject2;
	private static List<SearchResults> busRouteList;
	private static String boardingPointName;
	private static String boardingTime;
	
	public static List<SearchResults> parseFeed (String content) {

		BusRoute busRoute;
		SearchResults searchResults;
		
		try {
			
			if (content != null && content.length() > 0) {
				
				jsonArray = new JSONArray(content);
				busRouteList = new ArrayList<>();
				
				for (int i=0; i<jsonArray.length(); i++) {
					
					jsonObject1 = jsonArray.getJSONObject(i);
					
					jsonObject2 = jsonObject1.getJSONObject("busRoute");
					boardingPointName = jsonObject1.getString("boardingPointName");
					boardingTime = jsonObject1.getString("boardingTime");
					
					busRoute = new BusRoute();
					
					busRoute.setId(jsonObject2.getInt("id"));
					busRoute.setRouteNumber(jsonObject2.getString("routeNumber"));
					busRoute.setRouteName(jsonObject2.getString("routeName"));
					busRoute.setStartTime(jsonObject2.getString("startTime"));
					busRoute.setEndTime(jsonObject2.getString("endTime"));
					busRoute.setTotalSeats(jsonObject2.getInt("totalSeats"));
					busRoute.setDestinationCTSFacility(jsonObject2.getString("destinationCTSFacility"));
					busRoute.setNbrRegisteredUsers(jsonObject2.getInt("nbrRegisteredUsers"));
					busRoute.setSeatAvailable(jsonObject2.getBoolean("seatAvailable"));
					busRoute.setSession(jsonObject2.getString("session"));
					busRoute.setBusOwner(jsonObject2.getString("busOwner"));
					busRoute.setBusNumber(jsonObject2.getString("busNumber"));
					
					searchResults = new SearchResults();
					searchResults.setBusRoute(busRoute);
					searchResults.setBoardingPointName(boardingPointName);
					searchResults.setBoardingTime(boardingTime);
					
					busRouteList.add(searchResults);
				}

				return busRouteList;
			} else {
				System.out.println("The input content is null !");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}