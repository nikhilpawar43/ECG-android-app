package com.dissertation.easycommuteguideonline.model;

public class BusRouteBoardingPoint {
	
	private int id;
	
	private BusRoute busRoute;
	
	private BoardingPoint boardingPoint;
	
	private String haltTime;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public BusRoute getBusRoute() {
		return busRoute;
	}
	public void setBusRoute(BusRoute busRoute) {
		this.busRoute = busRoute;
	}

	public BoardingPoint getBoardingPoint() {
		return boardingPoint;
	}
	public void setBoardingPoint(BoardingPoint boardingPoint) {
		this.boardingPoint = boardingPoint;
	}

	public String getHaltTime() {
		return haltTime;
	}
	public void setHaltTime(String haltTime) {
		this.haltTime = haltTime;
	}
	
}
