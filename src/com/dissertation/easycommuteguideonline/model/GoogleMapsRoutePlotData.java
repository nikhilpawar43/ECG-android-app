package com.dissertation.easycommuteguideonline.model;

import java.util.List;

public class GoogleMapsRoutePlotData {

	private String googleMapRouteData;
	private String googleMapRouteData2;
	private String googleMapRouteData3;
	private List<BoardingPointMarker> boardingPointsMarkerList;
	
	public String getGoogleMapRouteData() {
		return googleMapRouteData;
	}
	public void setGoogleMapRouteData(String googleMapRouteData) {
		this.googleMapRouteData = googleMapRouteData;
	}
	
	public String getGoogleMapRouteData2() {
		return googleMapRouteData2;
	}
	public void setGoogleMapRouteData2(String googleMapRouteData2) {
		this.googleMapRouteData2 = googleMapRouteData2;
	}
	
	public String getGoogleMapRouteData3() {
		return googleMapRouteData3;
	}
	public void setGoogleMapRouteData3(String googleMapRouteData3) {
		this.googleMapRouteData3 = googleMapRouteData3;
	}
	
	public List<BoardingPointMarker> getBoardingPointsMarkerList() {
		return boardingPointsMarkerList;
	}
	public void setBoardingPointsMarkerList(List<BoardingPointMarker> boardingPointsMarkerList) {
		this.boardingPointsMarkerList = boardingPointsMarkerList;
	}
}
