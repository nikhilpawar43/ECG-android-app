package com.dissertation.easycommuteguideonline;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.dissertation.easycommuteguideonline.DataPackage.RequestPackage;
import com.dissertation.easycommuteguideonline.http.GooglePlacesAutocompleteAdapter;
import com.dissertation.easycommuteguideonline.model.Employee;
import com.dissertation.easycommuteguideonline.model.HttpManager;
import com.dissertation.easycommuteguideonline.model.Place;
import com.dissertation.easycommuteguideonline.server.ServerDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TravelToCTSActivity extends Activity implements LocationListener, OnItemClickListener {
	
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private Button searchBus, logoutButton;
	private AutoCompleteTextView autoComplete_tv;
	
	private TextView welcome_tv;
	private Employee user;
	private Spinner ctsFacilitySpinner;
	
	private LocationManager locationManager;
	private String provider;
	private double currentLatitude, currentLongitude;
	private String ctsFacility;
	private String findPlaceById;
	private String searchBusToCTSUrl = "http://localhost:8080/easycommuteguide/rest/searchBusToCTS";
	public static ArrayList<Place> resultList;
	private Location location;
	private String searchText;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private double[] latlng;
	private double sourceLatitude, sourceLongitude;
	private String uri;
	private RequestPackage requestPackage;
	
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE";
	
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.travel_to_cts);
		
		user = (Employee) getIntent().getExtras().get("User");
		
		welcome_tv = (TextView) findViewById(R.id.textView2);
		welcome_tv.setText("Welcome " + user.getEmployeeName() + ",");
		
		ctsFacilitySpinner = (Spinner) findViewById(R.id.spinner1);
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		autoComplete_tv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		searchBus = (Button) findViewById(R.id.button1);
		
		autoComplete_tv.setEnabled(false);
		autoComplete_tv.setAdapter(new GooglePlacesAutocompleteAdapter (this, R.layout.list_item));
		autoComplete_tv.setOnItemClickListener(this);
		
		logoutButton = (Button) findViewById(R.id.button2);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.clear();
				editor.commit();
				
				intent = new Intent(TravelToCTSActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		currentLatitude = 0.0;
		currentLongitude = 0.0;
		
//	Obtaining the user's current location using GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		
		if (provider != null && !provider.equals("")) {
			
			location = locationManager.getLastKnownLocation(provider);
			locationManager.requestLocationUpdates(provider, 20000, 1, this);
			
			if (location != null) {
				onLocationChanged(location);
				currentLatitude = location.getLatitude();
				currentLongitude = location.getLongitude();
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.putString("sourceLatitude", Double.toString(currentLatitude));
				editor.putString("sourceLongitude", Double.toString(currentLongitude));
				editor.commit();
				
				searchText = "Searching bus for latitude: " + currentLatitude + " and longitude: " + currentLongitude;
					
			} else {
				Toast.makeText(getBaseContext(), "Location cannot be retrived !", Toast.LENGTH_LONG).show();
			}
			
		} else {
			Toast.makeText(TravelToCTSActivity.this, "No provider found", Toast.LENGTH_SHORT).show();
		}
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
				radioButton = (RadioButton) findViewById(selectedRadioButtonId);
				
				System.out.println("The radio button text is: " + radioButton.getText());
				
				if (radioButton.getText().toString().equals("Current location")) {
					autoComplete_tv.setText("");
					autoComplete_tv.setEnabled(false);
					autoComplete_tv.setInputType(InputType.TYPE_NULL);
					autoComplete_tv.setFocusable(false);
					findPlaceById = "";
					
					if (location != null) {
						onLocationChanged(location);
						currentLatitude = location.getLatitude();
						currentLongitude = location.getLongitude();
						
						searchText = "Searching bus for latitude: " + currentLatitude + " and longitude: " + currentLongitude;
							
					} else {
						Toast.makeText(getBaseContext(), "Location cannot be retrived !", Toast.LENGTH_LONG).show();
					}
					
				} else if (radioButton.getText().toString().equals("Enter a location")) {
					autoComplete_tv.setEnabled(true);
					autoComplete_tv.setInputType(InputType.TYPE_CLASS_TEXT);
					autoComplete_tv.setFocusableInTouchMode(true);
					
					searchText = "";
				}
				
			}
		});
		
		searchBus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (searchText != null && searchText.length() == 0) {
					searchText = autoComplete_tv.getText().toString();
				}
				
				Toast.makeText(TravelToCTSActivity.this, searchText, Toast.LENGTH_LONG).show();
				
				searchBusToCTSUrl = "http://" + ServerDetails.SERVER_IP_ADDRESS + ":" + ServerDetails.SERVER_IP_PORT + "/easycommuteguide/rest/searchBusToCTS";
				
				ctsFacility = ctsFacilitySpinner.getSelectedItem().toString();
				
				requestPackage = new RequestPackage();
				requestPackage.setMethod("POST");
				requestPackage.setUri(searchBusToCTSUrl);
				requestPackage.setParam("ctsFacility", ctsFacility);
				
				if (searchText.contains("Searching")) {
					
					requestPackage.setParam("latitude", currentLatitude + "");
					requestPackage.setParam("longitude", currentLongitude + "");
					
					settings = getSharedPreferences("MYPREFS", 0);
					editor = settings.edit();
					editor.putString("sourcePlaceName", "Current location");
					editor.commit();
					
				} else {
					
					settings = getSharedPreferences("MYPREFS", 0);
					editor = settings.edit();
					editor.putString("sourcePlaceName", autoComplete_tv.getText().toString());
					editor.commit();
				
					requestPackage.setParam("placeid", findPlaceById);
				}
				
				MyTask task = new MyTask();
				task.execute(requestPackage);
			}
		});
		
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
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		System.out.println("The resultList is:" + TravelFomCTSActivity.resultList);
		if ( TravelFomCTSActivity.resultList != null && TravelFomCTSActivity.resultList.size()>0 ) {
			
			String place_name = (String) adapterView.getItemAtPosition(position);
			String place_id = null;
			for (Place place : TravelFomCTSActivity.resultList) {
				if (place.getName().equals(place_name)) {
					place_id = place.getPlace_id();
					
					break;
				}
			}
			
			if (place_id != null && !place_id.equals("")) {
				
				Toast.makeText(this, place_name , Toast.LENGTH_SHORT).show();
				findPlaceById = place_id;
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
	
	private void toastMessage (String message) {
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		
		System.out.println("The current latitude is: " + location.getLatitude() + " and longitude is: " + location.getLongitude());
		
		/*Toast.makeText(getBaseContext(), "The current latitude is: " + location.getLatitude() + " and longitude is: " + location.getLongitude(), Toast.LENGTH_LONG).show();*/
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
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
				System.out.println("The result is: \n\n" + result );
				/*Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();*/
				
				if (result != null && !result.equals("")) {
					
					if (findPlaceById != null && findPlaceById.length() > 0) {
						uri = "https://maps.googleapis.com/maps/api/place/details/json";
						
						requestPackage = new RequestPackage();
						requestPackage.setUri(uri);
						requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
						requestPackage.setParam("placeid", findPlaceById);
						
						LatLngTask task = new LatLngTask();
						task.execute(requestPackage);
					}
					
					intent = new Intent(TravelToCTSActivity.this, BusToCTSSearchActivity.class);
					intent.putExtra("busSearchResults", result);
					intent.putExtra("ctsFacility", ctsFacility);
					intent.putExtra("User", user);
					startActivity(intent);
					
				} else {
					toastMessage("Unable to find bus for this location !");
					Log.d("Easy commute guide online", "Result set is empty.");
				}
				
				
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
