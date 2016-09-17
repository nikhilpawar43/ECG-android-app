package com.dissertation.easycommuteguideonline.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BusRoute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String routeNumber;
	private String routeName;
	private String startTime;
	private String endTime;
	private int totalSeats;
	private String destinationCTSFacility;
	private int nbrRegisteredUsers;
	private boolean seatAvailable;
	private String session;
	private String busOwner;
	private String busNumber;
	
	/*@ManyToMany (mappedBy = "busRoutes")
	private List<BoardingPoint> boardingPoints = new ArrayList<>();*/
	
	private Set<BusRouteBoardingPoint> busRouteBoardingPoints = new HashSet<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	
	public String getDestinationCTSFacility() {
		return destinationCTSFacility;
	}
	public void setDestinationCTSFacility(String destinationCTSFacility) {
		this.destinationCTSFacility = destinationCTSFacility;
	}
	
	public int getNbrRegisteredUsers() {
		return nbrRegisteredUsers;
	}
	public void setNbrRegisteredUsers(int nbrRegisteredUsers) {
		this.nbrRegisteredUsers = nbrRegisteredUsers;
	}
	
	public boolean isSeatAvailable() {
		return seatAvailable;
	}
	public void setSeatAvailable(boolean seatAvailable) {
		this.seatAvailable = seatAvailable;
	}
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	
	public String getBusOwner() {
		return busOwner;
	}
	public void setBusOwner(String busOwner) {
		this.busOwner = busOwner;
	}
	
	public String getBusNumber() {
		return busNumber;
	}
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}
	
	public Set<BusRouteBoardingPoint> getBusRouteBoardingPoints() {
		return busRouteBoardingPoints;
	}
	public void setBusRouteBoardingPoints(Set<BusRouteBoardingPoint> busRouteBoardingPoints) {
		this.busRouteBoardingPoints = busRouteBoardingPoints;
	}
	
}
