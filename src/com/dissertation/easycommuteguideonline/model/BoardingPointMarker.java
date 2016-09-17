package com.dissertation.easycommuteguideonline.model;

public class BoardingPointMarker {
	
	private String boardingPointName;
	private double latitude;
	private double longitude;
	private double distance;
	private double time;

	public String getBoardingPointName() {
		return boardingPointName;
	}
	public void setBoardingPointName(String boardingPointName) {
		this.boardingPointName = boardingPointName;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}

}
