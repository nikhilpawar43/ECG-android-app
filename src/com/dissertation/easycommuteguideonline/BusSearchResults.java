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
import android.widget.Toast;

public class BusSearchResults extends Activity {
	
	private Employee user;
	private String busSearchResult;
	private List<SearchResults> busRouteList;
	
	private TextView time1_tv, time2_tv, time3_tv, welcome_tv;
	private TextView routeName1_tv, routeName2_tv, routeName3_tv;
	private Button routeNo1_btn, routeNo2_btn, routeNo3_btn, logoutButton;
	private TextView boardingPoint1_tv, boardingPoint2_tv, boardingPoint3_tv;
	
	private Intent intent;
	private BusRoute busRoute = null;
	
	private int[] busids = new int[3];
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_search_results);
		
		busSearchResult = getIntent().getStringExtra("busSearchResults");
		busRouteList = BusSearchJSONParser.parseFeed(busSearchResult);
		
		user = (Employee) getIntent().getExtras().get("User");
		welcome_tv = (TextView) findViewById(R.id.textView3);
		welcome_tv.setText("Welcome " + user.getEmployeeName() + ",");
		
		logoutButton = (Button) findViewById(R.id.button4);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.clear();
				editor.commit();
				
				intent = new Intent(BusSearchResults.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		time1_tv = (TextView) findViewById(R.id.textView7);
		routeName1_tv = (TextView) findViewById(R.id.textView9);
		routeNo1_btn = (Button) findViewById(R.id.button1);
		boardingPoint1_tv = (TextView) findViewById(R.id.textView10);
		
		time2_tv = (TextView) findViewById(R.id.textView19);
		routeName2_tv = (TextView) findViewById(R.id.textView21);
		routeNo2_btn = (Button) findViewById(R.id.button2);
		boardingPoint2_tv = (TextView) findViewById(R.id.textView22);
		
		time3_tv = (TextView) findViewById(R.id.textView23);
		routeName3_tv = (TextView) findViewById(R.id.textView25);
		routeNo3_btn = (Button) findViewById(R.id.button3);
		boardingPoint3_tv = (TextView) findViewById(R.id.textView26);
		
		
		for (int i = 0; i < busRouteList.size(); i++) {
			busRoute = busRouteList.get(i).getBusRoute();
			
			switch (busRoute.getStartTime()) {
			case "18:15":
				
				busids[0] = busRoute.getId();
				
				time1_tv.setText(busRoute.getStartTime());
				routeNo1_btn.setText(busRoute.getRouteNumber());
				
				routeName1_tv.setText(busRoute.getRouteName());
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.putString("targetBoardingPoint", boardingPoint1_tv.getText().toString());
				editor.commit();
				
				routeNo1_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						/*intent = new Intent(BusSearchResults.this, GoogleMapActivity.class);
						startActivity(intent);*/
						
						requestData (busids[0], boardingPoint1_tv.getText().toString());
					}
				});
				
				boardingPoint1_tv.setText(busRouteList.get(i).getBoardingPointName());
				break;
				
			case "19:30":
				
				busids[1] = busRoute.getId();
				
				time2_tv.setText(busRoute.getStartTime());
				routeName2_tv.setText(busRoute.getRouteName());
				routeNo2_btn.setText(busRoute.getRouteNumber());
				boardingPoint2_tv.setText(busRouteList.get(i).getBoardingPointName());
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.putString("targetBoardingPoint", boardingPoint2_tv.getText().toString());
				editor.commit();
				
				routeNo2_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						/*intent = new Intent(BusSearchResults.this, GoogleMapActivity.class);
						startActivity(intent);*/
						
						requestData (busids[1], boardingPoint2_tv.getText().toString());
					}
				});
				
				break;
				
			case "19:15":
				
				busids[1] = busRoute.getId();
				
				time2_tv.setText(busRoute.getStartTime());
				routeName2_tv.setText(busRoute.getRouteName());
				routeNo2_btn.setText(busRoute.getRouteNumber());
				boardingPoint2_tv.setText(busRouteList.get(i).getBoardingPointName());
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.putString("targetBoardingPoint", boardingPoint2_tv.getText().toString());
				editor.commit();
				
				routeNo2_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						/*intent = new Intent(BusSearchResults.this, GoogleMapActivity.class);
						startActivity(intent);*/
						
						requestData (busids[1], boardingPoint2_tv.getText().toString());
					}
				});
				
				break;

			case "21:00":
				
				busids[2] = busRoute.getId();
				
				time3_tv.setText(busRoute.getStartTime());
				routeName3_tv.setText(busRoute.getRouteName());
				routeNo3_btn.setText(busRoute.getRouteNumber());
				boardingPoint3_tv.setText(busRouteList.get(i).getBoardingPointName());
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.putString("targetBoardingPoint", boardingPoint3_tv.getText().toString());
				editor.commit();
				
				routeNo3_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						/*intent = new Intent(BusSearchResults.this, GoogleMapActivity.class);
						startActivity(intent);*/
						
						requestData (busids[2], boardingPoint3_tv.getText().toString());
					}
				});
				
				break;

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
	
	private void requestData(int busid, String boardingPointName) {
		
		RequestPackage requestPackage = new RequestPackage();
		requestPackage.setMethod("GET");
		requestPackage.setUri("http://" + ServerDetails.SERVER_IP_ADDRESS + ":" + ServerDetails.SERVER_IP_PORT + "/easycommuteguide/rest/getGoogleMapRoute");
		requestPackage.setParam("busid", busid + "");
		requestPackage.setParam("boardingPointName", boardingPointName);
		
		MyTask task = new MyTask();
		task.execute(requestPackage);
	}
	
	@SuppressWarnings("unused")
	private void toastMessage (String message) {
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
			
			intent = new Intent(BusSearchResults.this, GoogleMapActivity.class);
			intent.putExtra("busRoutePlotDetails", result);
			startActivity(intent);

		}
		
	}
}
