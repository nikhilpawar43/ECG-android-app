package com.dissertation.easycommuteguideonline.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dissertation.easycommuteguideonline.model.BoardingPointMarker;
import com.dissertation.easycommuteguideonline.model.GoogleMapsRoutePlotData;

public class BusRoutePlotDataParser {
	
	public static GoogleMapsRoutePlotData parseFeed(String content) {
		
		JSONArray jsonArray;
		JSONObject jsonObject1, jsonObject2;
		String googleMapRouteData, googleMapRouteData2;
		GoogleMapsRoutePlotData routePlotData;
		List<BoardingPointMarker> boardingPointsList;
		BoardingPointMarker boardingPointMarker;
		
		try {
		
			jsonObject1 = new JSONObject(content);
			googleMapRouteData = jsonObject1.getString("googleMapRouteData");
			googleMapRouteData2 = jsonObject1.getString("googleMapRouteData2");
			
			jsonArray = jsonObject1.getJSONArray("boardingPointsMarkerList");
			
			boardingPointsList = new ArrayList<>();
			
			for (int i=0; i<jsonArray.length(); i++) {
				
				jsonObject2 = jsonArray.getJSONObject(i);
				
				boardingPointMarker = new BoardingPointMarker();
				boardingPointMarker.setBoardingPointName( jsonObject2.getString("boardingPointName") );
				boardingPointMarker.setLatitude( jsonObject2.getDouble("latitude") );
				boardingPointMarker.setLongitude( jsonObject2.getDouble("longitude") );
				boardingPointMarker.setDistance( jsonObject2.getDouble("distance") );
				boardingPointMarker.setTime( jsonObject2.getDouble("time") );
			
				boardingPointsList.add(boardingPointMarker);
			}
			
			routePlotData = new GoogleMapsRoutePlotData();
			routePlotData.setGoogleMapRouteData(googleMapRouteData);
			routePlotData.setGoogleMapRouteData2(googleMapRouteData2);
			routePlotData.setBoardingPointsMarkerList(boardingPointsList);
			
			return routePlotData;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
