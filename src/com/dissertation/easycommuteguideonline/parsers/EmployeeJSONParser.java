package com.dissertation.easycommuteguideonline.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dissertation.easycommuteguideonline.model.Employee;

public class EmployeeJSONParser {
	
	private static List<Employee> employees;
	private static JSONArray jsonArray;
	private static JSONObject jsonObject;
	private static Employee employee;
	private static String registrationDate;
	
	public static List<Employee> parseFeed(String content) {
		
		try {
			jsonArray = new JSONArray(content);
			employees = new ArrayList<>();
			
			for (int i=0; i<jsonArray.length(); i++) {
				
				jsonObject = jsonArray.getJSONObject(i);
				employee = new Employee();
				
				employee.setId(jsonObject.getInt("id"));
				employee.setPassword(jsonObject.getString("password"));
				employee.setEmployeeName(jsonObject.getString("employeeName"));
				
				registrationDate =  jsonObject.getString("registeredDate");
				employee.setRegisteredDate(registrationDate);
				
				employees.add(employee);
			}
			
			return employees;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
