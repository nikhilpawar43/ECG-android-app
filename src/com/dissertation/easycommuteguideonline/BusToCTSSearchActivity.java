package com.dissertation.easycommuteguideonline;

import java.util.List;

import com.dissertation.easycommuteguideonline.DataPackage.RequestPackage;
import com.dissertation.easycommuteguideonline.model.BusRoute;
import com.dissertation.easycommuteguideonline.model.Employee;
import com.dissertation.easycommuteguideonline.model.HttpManager;
import com.dissertation.easycommuteguideonline.model.SearchResults;
import com.dissertation.easycommuteguideonline.parsers.BusSearchJSONParser;
import com.dissertation.easycommuteguideonline.server.ServerDetails;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BusToCTSSearchActivity extends Activity {

	private Employee user;
	private TextView ctsFacility_tv;
	private TextView time1_tv, welcome_tv;
	private Button routeName1_btn, logoutButton;
	private TextView routeNo1_tv;
	private TextView boardingPoint1_tv;
	
	private String ctsFacility;
	private String busSearchResult;
	private List<SearchResults> busRouteList;
	private BusRoute busRoute = null;
	private int busid;
	private String boardingPointName;
	private Intent intent;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_to_cts_search);
		
		ctsFacility = getIntent().getStringExtra("ctsFacility");
		busSearchResult = getIntent().getStringExtra("busSearchResults");
		
		user = (Employee) getIntent().getExtras().get("User");
		welcome_tv = (TextView) findViewById(R.id.textView5);
		welcome_tv.setText("Welcome " + user.getEmployeeName() + ",");
		
		busRouteList = BusSearchJSONParser.parseFeed(busSearchResult);
		
		time1_tv = (TextView) findViewById(R.id.textView4);
		routeNo1_tv = (TextView) findViewById(R.id.textView8);
		boardingPoint1_tv = (TextView) findViewById(R.id.textView10);
		ctsFacility_tv = (TextView) findViewById(R.id.textView12);
		routeName1_btn = (Button) findViewById(R.id.button1);
		
		logoutButton = (Button) findViewById(R.id.button3);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.clear();
				editor.commit();
				
				intent = new Intent(BusToCTSSearchActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		ctsFacility_tv.setText(ctsFacility);
		
		if (busRouteList != null && busRouteList.size() > 0){
			
			busRoute = busRouteList.get(0).getBusRoute();
			
			busid = busRoute.getId();
			
			time1_tv.setText(busRouteList.get(0).getBoardingTime());
			routeNo1_tv.setText(busRoute.getRouteName());
			routeName1_btn.setText(busRoute.getRouteNumber());
			
			boardingPointName = busRouteList.get(0).getBoardingPointName();
			
			settings = getSharedPreferences("MYPREFS", 0);
			editor = settings.edit();
			editor.putString("targetBoardingPoint", boardingPointName);
			editor.putString("session", busRoute.getSession());
			editor.putString("ctsFacility", ctsFacility);
			editor.commit();
			
			boardingPoint1_tv.setText(boardingPointName);
			
			routeName1_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					/*intent = new Intent(BusSearchResults.this, GoogleMapActivity.class);
					startActivity(intent);*/
					
					requestData (busid, boardingPointName);
				}
			});
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
	
	private void requestData(int busid, String boardingPointName) {
		
		RequestPackage requestPackage = new RequestPackage();
		requestPackage.setMethod("GET");
		requestPackage.setUri("http://" + ServerDetails.SERVER_IP_ADDRESS + ":" + ServerDetails.SERVER_IP_PORT + "/easycommuteguide/rest/getGoogleMapRouteToCTS");
		requestPackage.setParam("busid", busid + "");
		requestPackage.setParam("boardingPointName", boardingPointName);
		
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
			/*googleMapsRoutePlotData = BusRoutePlotDataParser.parseFeed(content);
			return googleMapsRoutePlotData;*/
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			/*List<BoardingPointMarker> boardingPointsList = result.getBoardingPointsMarkerList();
			
			for (BoardingPointMarker boardingPoint : boardingPointsList) {
				System.out.println(boardingPoint.getBoardingPointName());
			}*/
			
			intent = new Intent(BusToCTSSearchActivity.this, GoogleMaptToCTSActivity.class);
			intent.putExtra("busRoutePlotDetails", result);
			startActivity(intent);

		}
		
	}
}
