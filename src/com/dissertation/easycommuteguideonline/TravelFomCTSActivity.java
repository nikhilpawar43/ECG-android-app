package com.dissertation.easycommuteguideonline;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.dissertation.easycommuteguideonline.DataPackage.RequestPackage;
import com.dissertation.easycommuteguideonline.http.GooglePlacesAutocompleteAdapter;
import com.dissertation.easycommuteguideonline.model.Employee;
import com.dissertation.easycommuteguideonline.model.HttpManager;
import com.dissertation.easycommuteguideonline.model.Place;
import com.dissertation.easycommuteguideonline.model.SearchResults;
import com.dissertation.easycommuteguideonline.parsers.BusSearchJSONParser;
import com.dissertation.easycommuteguideonline.server.ServerDetails;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TravelFomCTSActivity extends Activity implements OnItemClickListener {
	
	private TextView welcome_tv;
	private Employee user;
	private AutoCompleteTextView autoComplete_tv;
	private Button searchBusesButton, logoutButton;
	private Spinner ctsFacilitySpinner;
	
	private String ctsFacility, destination, destinationPlaceID;
	
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE";
	
	public static ArrayList<Place> resultList;
	private static List<SearchResults> busRouteList;
	private String sourcePlaceName;
	private RequestPackage requestPackage;
	private String uri;
	private double[] latlng;
	private double sourceLatitude, sourceLongitude;
	
	private Intent intent;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.travel_from_cts);
		
		sourcePlaceName = "";
		
		user = (Employee) getIntent().getExtras().get("User");
		
		welcome_tv = (TextView) findViewById(R.id.textView1);
		welcome_tv.setText("Welcome " + user.getEmployeeName() + ",");
		
		autoComplete_tv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		
		autoComplete_tv.setAdapter(new GooglePlacesAutocompleteAdapter (this, R.layout.list_item));
		autoComplete_tv.setOnItemClickListener(this);
		
		ctsFacilitySpinner = (Spinner) findViewById(R.id.spinner1);
		logoutButton = (Button) findViewById(R.id.button2);
		
		searchBusesButton = (Button) findViewById(R.id.button1);
		searchBusesButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ctsFacility = ctsFacilitySpinner.getSelectedItem().toString();
				destination = autoComplete_tv.getText().toString();
				
				for (Place place : resultList) {
					if (place.getName().equals(destination)) {
						
						destinationPlaceID = place.getPlace_id();
						sourcePlaceName = place.getName();
						break;
					}
				}

				if (ctsFacility != null && destination != null && destinationPlaceID != null && sourcePlaceName.length()>0) {
					
					uri = "https://maps.googleapis.com/maps/api/place/details/json";
					
					requestPackage = new RequestPackage();
					requestPackage.setUri(uri);
					requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
					requestPackage.setParam("placeid", destinationPlaceID);
					
					LatLngTask task = new LatLngTask();
					task.execute(requestPackage);
					
					settings = getSharedPreferences("MYPREFS", 0);
					editor = settings.edit();
					editor.putString("ctsFacility", ctsFacility);
					
					editor.putString("sourcePlaceName", sourcePlaceName);
					editor.commit();
					
					getBusSearchResult ("http://" + ServerDetails.SERVER_IP_ADDRESS + ":" + ServerDetails.SERVER_IP_PORT + "/easycommuteguide/rest/searchBuses", ctsFacility, destination, destinationPlaceID);
				} else {
					toastMessage("Please select the CTS facility and a destination place !");
				}
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.clear();
				editor.commit();
				
				intent = new Intent(TravelFomCTSActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if ( resultList != null && resultList.size()>0 ) {
			
			String place_name = (String) adapterView.getItemAtPosition(position);
			String place_id = null;
			for (Place place : resultList) {
				if (place.getName().equals(place_name)) {
					place_id = place.getPlace_id();
					break;
				}
			}
			
			if (place_id != null && !place_id.equals("")) {
				
				Toast.makeText(this, place_name , Toast.LENGTH_SHORT).show();
				
				StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
				sb.append("?key=" + API_KEY);
				try {
					sb.append("&placeid=" + URLEncoder.encode(place_id, "utf8"));
				} catch (UnsupportedEncodingException e) {
					System.out.println("The UnsupportedEncodingException is: " + e.getMessage());
				}
				
			}
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void toastMessage (String message) {
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private void getBusSearchResult (String URI, String ctsFacility, String destinationName, String destinationPlaceID) {
		
		RequestPackage requestPackage = new RequestPackage();
		requestPackage.setMethod("POST");
		requestPackage.setUri(URI);
		requestPackage.setParam("ctsFacility", ctsFacility);
		requestPackage.setParam("destinationName", destinationName);
		requestPackage.setParam("destinationPlaceID", destinationPlaceID);
		
		MyTask task = new MyTask();
		task.execute(requestPackage);
	}
	
	private class MyTask extends AsyncTask<RequestPackage, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(RequestPackage... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result != null && !result.equals("")) {

				busRouteList = BusSearchJSONParser.parseFeed(result);
				
				String searchResult = null;
				for (SearchResults busRoute : busRouteList) {
					if (searchResult == null) 
						searchResult = busRoute.getBusRoute().getRouteName() + "\n";
					else
					searchResult = searchResult + busRoute.getBusRoute().getRouteName() + "\n";
				}
				
				toastMessage(searchResult);
				
				intent = new Intent(TravelFomCTSActivity.this, BusSearchResults.class);
				intent.putExtra("busSearchResults", result);
				intent.putExtra("User", user);
				startActivity(intent);
				
			} else {
				toastMessage("Unable to find bus for this location !");
				Log.d("Easy commute guide online", "Result set is empty.");
			}
			
		}
	}
	
private class LatLngTask extends AsyncTask<RequestPackage, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(RequestPackage... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result != null && !result.equals("")) {

				latlng = HttpManager.parseDestinationLocationDetails(result);
				sourceLatitude = latlng[0];
				sourceLongitude = latlng[1];
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.putString("sourceLatitude", Double.toString(sourceLatitude));
				editor.putString("sourceLongitude", Double.toString(sourceLongitude));
				editor.commit();
				
			} else {
				toastMessage("Unable to find bus for this location !");
				Log.d("Easy commute guide online", "Result set is empty.");
			}
			
		}
	}
	
}


