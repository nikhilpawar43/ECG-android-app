package com.dissertation.easycommuteguideonline;

import java.util.List;

import com.dissertation.easycommuteguideonline.DataPackage.RequestPackage;
import com.dissertation.easycommuteguideonline.model.Employee;
import com.dissertation.easycommuteguideonline.model.HttpManager;
import com.dissertation.easycommuteguideonline.parsers.EmployeeJSONParser;
import com.dissertation.easycommuteguideonline.server.ServerDetails;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText userid, password;
	private Button login;
	private List<Employee> employees;
	private Employee user;
	private String userid_txt, password_txt;
	private Intent intent;
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		userid = (EditText) findViewById(R.id.userid_et);
		password = (EditText) findViewById(R.id.password_et);
		
		login = (Button) findViewById(R.id.login_bn);
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				if (isOnline()) {
//					requestData("http://services.hanselandpetal.com/feeds/flowers.json");
					
					if (googleServices()) {	
						userid_txt = userid.getText().toString();
						password_txt = password.getText().toString();
//						requestData("http://192.168.1.102:8080/easycommuteguide/rest/login");
						requestData("http://" + ServerDetails.SERVER_IP_ADDRESS + ":" + ServerDetails.SERVER_IP_PORT + "/easycommuteguide/rest/login");
					}
				} else {
					toastMessage("Network not available.");
				}
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
	
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if(netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean googleServices () {
		
		int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		} else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		} else {
			toastMessage("Can't connect to Google Play Services.");
		}
		return false;
		
	}
	
	private void requestData(String URI) {
		
		RequestPackage requestPackage = new RequestPackage();
		requestPackage.setMethod("GET");
		requestPackage.setUri(URI);
		requestPackage.setParam("userid", userid_txt);
		requestPackage.setParam("password", password_txt);
		
		MyTask task = new MyTask();
		task.execute(requestPackage);
	}
	
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
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result != null && !result.equals("")) {
				employees = EmployeeJSONParser.parseFeed(result);
				if (employees.size() > 0) {
					user = employees.get(0);
					toastMessage("User: " + user.getId() + " authenticated successfully.");
					
					intent = new Intent(MainActivity.this, UserHomeActivity.class);
					intent.putExtra("User", user);
					startActivity(intent);
				}
			} else {
				toastMessage("Invalid username or password");
				Log.d("Easy commute guide online", "Result set is empty.");
			}
		}
		
	}
}
