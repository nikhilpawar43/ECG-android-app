package com.dissertation.easycommuteguideonline.model;

public class SearchResults {
	
	private BusRoute busRoute;
	private String boardingPointName;
	private String boardingTime;

	public BusRoute getBusRoute() {
		return busRoute;
	}
	public void setBusRoute(BusRoute busRoute) {
		this.busRoute = busRoute;
	}
	
	public String getBoardingPointName() {
		return boardingPointName;
	}
	public void setBoardingPointName(String boardingPointName) {
		this.boardingPointName = boardingPointName;
	}
	
	public String getBoardingTime() {
		return boardingTime;
	}
	public void setBoardingTime(String boardingTime) {
		this.boardingTime = boardingTime;
	}

}