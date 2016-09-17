package com.dissertation.easycommuteguideonline;

import com.dissertation.easycommuteguideonline.DataPackage.RequestPackage;
import com.dissertation.easycommuteguideonline.model.BusRegistrationDetails;
import com.dissertation.easycommuteguideonline.model.Employee;
import com.dissertation.easycommuteguideonline.model.HttpManager;
import com.dissertation.easycommuteguideonline.parsers.RegistrationDetailsJSONParser;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserHomeActivity extends Activity {
	
	private Button travelToCTSButton, travelFromCTSButton, logoutButton, viewRegisteredDetailsButton;
	private Intent intent;
	private Employee user;
	private TextView welcome_tv;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	private RequestPackage requestPackage;
	private BusRegistrationDetails busRegistrationDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_home);
		
		travelToCTSButton = (Button) findViewById(R.id.button1);
		travelFromCTSButton = (Button) findViewById(R.id.button2);
		logoutButton = (Button) findViewById(R.id.button3);
		viewRegisteredDetailsButton = (Button) findViewById(R.id.button4);
		
		user = (Employee) getIntent().getExtras().get("User");
		welcome_tv = (TextView) findViewById(R.id.textView2);
		welcome_tv.setText("Welcome " + user.getEmployeeName() + ",");

		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.clear();
				editor.commit();
				
				intent = new Intent(UserHomeActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		travelFromCTSButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				intent = new Intent(UserHomeActivity.this, TravelFomCTSActivity.class);
				intent.putExtra("User", user);
				startActivity(intent);
			}
		});
		
		travelToCTSButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				intent = new Intent(UserHomeActivity.this, TravelToCTSActivity.class);
				intent.putExtra("User", user);
				startActivity(intent);
			}
		});
		
		viewRegisteredDetailsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				requestData("http://" + ServerDetails.SERVER_IP_ADDRESS + ":" + ServerDetails.SERVER_IP_PORT + "/easycommuteguide/rest/viewRegisteredDetails");
				
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
	
	private void toastMessage (String message) {
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private void requestData(String URI) {
		
		requestPackage = new RequestPackage();
		requestPackage.setMethod("GET");
		requestPackage.setUri(URI);
		requestPackage.setParam("userid", user.getId() + "");
		
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
				
				busRegistrationDetails = RegistrationDetailsJSONParser.parseFeed(result);
				
				intent = new Intent(UserHomeActivity.this, ViewRegisteredDetailsActivity.class);
				intent.putExtra("User", user);
				intent.putExtra("boardingPointName", busRegistrationDetails.getBoardingPointName());
				intent.putExtra("morningBusRouteno", busRegistrationDetails.getMorningBusRouteno());
				intent.putExtra("morningBusRoutename", busRegistrationDetails.getMorningBusRoutename());
				intent.putExtra("eveningBusRouteno", busRegistrationDetails.getEveningBusRouteno());
				intent.putExtra("eveningBusRoutename", busRegistrationDetails.getEveningBusRoutename());
				
				startActivity(intent);
				
			} else {
				toastMessage("Registration details are empty !");
				Log.d("Easy commute guide online", "Result set is empty.");
			}
		}
		
	}
}
