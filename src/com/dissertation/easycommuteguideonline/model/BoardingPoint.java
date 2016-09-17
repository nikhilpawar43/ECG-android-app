package com.dissertation.easycommuteguideonline.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoardingPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String boardingPointName;
	
	private Double lattitude;
	
	private Double longitude;
	
	private Double cost;
	
	private int nbrRegisteredUsers;
	
	private List<Employee> registeredEmployees = new ArrayList<>();
	
	/*@ManyToMany
	private List<BusRoute> busRoutes = new ArrayList<>();	*/
	
	private Set<BusRouteBoardingPoint> busRouteBoardingPoints = new HashSet<>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getBoardingPointName() {
		return boardingPointName;
	}
	public void setBoardingPointName(String boardingPointName) {
		this.boardingPointName = boardingPointName;
	}

	public Double getLattitude() {
		return lattitude;
	}
	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}

	public int getNbrRegisteredUsers() {
		return nbrRegisteredUsers;
	}
	public void setNbrRegisteredUsers(int nbrRegisteredUsers) {
		this.nbrRegisteredUsers = nbrRegisteredUsers;
	}

	public List<Employee> getRegisteredEmployees() {
		return registeredEmployees;
	}
	public void setRegisteredEmployees(List<Employee> registeredEmployees) {
		this.registeredEmployees = registeredEmployees;
	}
	
	public Set<BusRouteBoardingPoint> getBusRouteBoardingPoints() {
		return busRouteBoardingPoints;
	}
	public void setBusRouteBoardingPoints(Set<BusRouteBoardingPoint> busRouteBoardingPoints) {
		this.busRouteBoardingPoints = busRouteBoardingPoints;
	}
	
}
