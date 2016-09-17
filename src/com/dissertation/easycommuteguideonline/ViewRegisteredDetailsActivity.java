package com.dissertation.easycommuteguideonline;

import com.dissertation.easycommuteguideonline.model.Employee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewRegisteredDetailsActivity extends Activity  {

	private Intent intent;
	private Employee user;
	private TextView welcome_tv;
	private TextView boardingPointName_tv, morningBusNo_tv, morningBusName_tv, eveningBusNo_tv, eveningBusName_tv;
	private Button logoutButton;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_registered_details);
		
		user = (Employee) getIntent().getExtras().get("User");
		welcome_tv = (TextView) findViewById(R.id.textView2);
		welcome_tv.setText("Welcome " + user.getEmployeeName() + ",");
		
		boardingPointName_tv = (TextView) findViewById(R.id.textView4);
		morningBusNo_tv = (TextView) findViewById(R.id.textView6);
		morningBusName_tv = (TextView) findViewById(R.id.textView9);
		eveningBusNo_tv = (TextView) findViewById(R.id.textView8);
		eveningBusName_tv = (TextView) findViewById(R.id.textView10);
		
		boardingPointName_tv.setText(getIntent().getExtras().getString("boardingPointName"));
		morningBusNo_tv.setText(getIntent().getExtras().getString("morningBusRouteno"));
		morningBusName_tv.setText(getIntent().getExtras().getString("morningBusRoutename"));
		eveningBusNo_tv.setText(getIntent().getExtras().getString("eveningBusRouteno"));
		eveningBusName_tv.setText(getIntent().getExtras().getString("eveningBusRoutename"));
		
		logoutButton = (Button) findViewById(R.id.button1);
		
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				settings = getSharedPreferences("MYPREFS", 0);
				editor = settings.edit();
				editor.clear();
				editor.commit();
				
				intent = new Intent(ViewRegisteredDetailsActivity.this, MainActivity.class);
				startActivity(intent);
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
}
