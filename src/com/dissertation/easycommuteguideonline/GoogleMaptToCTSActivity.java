package com.dissertation.easycommuteguideonline;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.dissertation.easycommuteguideonline.model.BoardingPointMarker;
import com.dissertation.easycommuteguideonline.model.GoogleMapsRoutePlotData;
import com.dissertation.easycommuteguideonline.parsers.BusRoutePlotDataParser;
import com.dissertation.easycommuteguideonline.parsers.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GoogleMaptToCTSActivity extends FragmentActivity {
	
	private GoogleMap map;
	private SupportMapFragment fm;
	private LatLng destinationLatLng;
	private static final float DEFAULT_ZOOM = 14;
	private static final LatLng DLF_QUADRON = new LatLng(18.585637, 73.696155);
	private static final LatLng HINJEWADI_PHASE_1 = new LatLng(18.583008, 73.734361);
	private String ctsFacility;
	private String sourcePlaceName;
	
	private String googleMapsRouteData;
	private SharedPreferences settings;
	private double sourceLatitude, sourceLongitude;
	
	private MarkerOptions markerOptions;
	private Marker sourceMarker;
	private double distance, time;
	private DecimalFormat df;
	
	private TextView tv_boardingPointName, tv_snippet;
	
	private GoogleMapsRoutePlotData googleMapsRoutePlotData;
	private BoardingPointMarker sourceBoardingPointMarker;  
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);
        
        googleMapsRouteData = getIntent().getExtras().getString("busRoutePlotDetails");
        
        System.out.println("Inside Google Maps Activity");
        System.out.println();
        
        googleMapsRoutePlotData = BusRoutePlotDataParser.parseFeed(googleMapsRouteData);
        
		List<BoardingPointMarker> boardingPointsList = googleMapsRoutePlotData.getBoardingPointsMarkerList();
		
		sourceBoardingPointMarker = boardingPointsList.get(0);
		
		for (BoardingPointMarker boardingPoint : boardingPointsList) {
			System.out.println(boardingPoint.getBoardingPointName());
		}
        
        /*System.out.println(googleMapsRouteData);*/
        
//    Getting reference to SupportMapFragment of the activity_main fragment-1
        fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment1);
        
//    Getting Map for the SupportMapFragment
        map = fm.getMap();
        
        if(map!=null) {
        	
        	map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
				
				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}
				
				@SuppressLint("InflateParams")
				@Override
				public View getInfoContents(Marker marker) {
					
					View view = getLayoutInflater().inflate(R.layout.boardingpoint_info_window, null);
					
					tv_boardingPointName = (TextView) view.findViewById(R.id.textView1);
					tv_snippet = (TextView) view.findViewById(R.id.textView2);
					
					System.out.println("The market title is: " + marker.getTitle());
					
					tv_boardingPointName.setText(marker.getTitle());
					tv_snippet.setText(marker.getSnippet());
					
					return view;
				}
			});
        	
        	settings = getSharedPreferences("MYPREFS", 0);
        	ctsFacility = settings.getString("ctsFacility", "");
        	
        	if (ctsFacility.equals("DLF Quadron Phase-2")) {
        		destinationLatLng = DLF_QUADRON;
        	} else if (ctsFacility.equals("CTS Hinjewadi Phase-1")) {
        		destinationLatLng = HINJEWADI_PHASE_1;
        	}
        	
        	settings = getSharedPreferences("MYPREFS", 0);
        	sourceLatitude = Double.parseDouble(settings.getString("sourceLatitude", "0.0"));
        	sourceLongitude = Double.parseDouble(settings.getString("sourceLongitude", "0.0"));
        	sourcePlaceName = settings.getString("sourcePlaceName", "");
            
            gotoLocation(sourceBoardingPointMarker.getLatitude(), sourceBoardingPointMarker.getLongitude(), DEFAULT_ZOOM);

            if (sourceMarker != null) {
            	sourceMarker.remove();
            }
            
//        Create source location marker
/*        	markerOptions = new MarkerOptions()
        			.title(ctsFacility)
        			.position(destinationLatLng)
        			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));*/
        	
//        Add the source marker on Google Maps	
//         	sourceMarker = map.addMarker(markerOptions);
        	
        	distance = 0.0;
        	time = 0.0;
        	
        	df = new DecimalFormat();
        	df.setMaximumFractionDigits(2);
        	
//        Create bus halt point markers along with last stop.
        	for (int i=0; i<boardingPointsList.size(); i++) {
        		
        		BoardingPointMarker boardingPoint = boardingPointsList.get(i);
//        	for (BoardingPointMarker boardingPoint : boardingPointsList) {
        		
        		LatLng ll = new LatLng(boardingPoint.getLatitude(), boardingPoint.getLongitude());
        		
        		distance = distance + boardingPoint.getDistance();
        		time = time + boardingPoint.getTime();
        		
        		if (i==0) {
        			
        			markerOptions = new MarkerOptions()
        					.title(boardingPoint.getBoardingPointName())
        					.position(ll)
        					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker));
        			
        		} if ( i == boardingPointsList.size()-1) {
        			
        			markerOptions = new MarkerOptions()
					.title(boardingPoint.getBoardingPointName())
					.position(ll)
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        			
        		} else if ( i>0 && i<boardingPointsList.size()-1)  {
        			
        			markerOptions = new MarkerOptions()
    					.title(boardingPoint.getBoardingPointName())
    					.position(ll)
    					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        		}
    			
    			markerOptions.snippet("Distance: " + df.format(distance) + " Kms \n" + "Time: " + time + " mins.");
        		
        		map.addMarker(markerOptions);
        		
        	}
        	
        	markerOptions = new MarkerOptions()
        			.title(sourcePlaceName)
        			.position(new LatLng(sourceLatitude, sourceLongitude))
        			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        	
        	map.addMarker(markerOptions);
        	
            ParserTask parserTask = new ParserTask();
            parserTask.execute(googleMapsRoutePlotData.getGoogleMapRouteData());
 
            if (googleMapsRoutePlotData.getGoogleMapRouteData2() != null && googleMapsRoutePlotData.getGoogleMapRouteData2().length() > 0 ) {
	            parserTask = new ParserTask();
	            parserTask.execute(googleMapsRoutePlotData.getGoogleMapRouteData2());
            }
            
            if (googleMapsRoutePlotData.getGoogleMapRouteData3() != null && googleMapsRoutePlotData.getGoogleMapRouteData3().length() > 0 ) {
	            parserTask = new ParserTask();
	            parserTask.execute(googleMapsRoutePlotData.getGoogleMapRouteData3());
            }
            
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	 
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
 
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
 
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute (List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
 
            // Traversing through all the routes
            if (result != null) {
            	
            	for(int i=0;i<result.size();i++){
            		points = new ArrayList<LatLng>();
            		lineOptions = new PolylineOptions();
            		
            		// Fetching i-th route
            		List<HashMap<String, String>> path = result.get(i);
            		
            		// Fetching all the points in i-th route
            		for(int j=0;j<path.size();j++){
            			HashMap<String,String> point = path.get(j);
            			
            			double lat = Double.parseDouble(point.get("lat"));
            			double lng = Double.parseDouble(point.get("lng"));
            			LatLng position = new LatLng(lat, lng);
            			
            			points.add(position);
            		}
            		
            		// Adding all the points in the route to LineOptions
            		lineOptions.addAll(points);
            		lineOptions.width(5);
            		lineOptions.color(Color.RED);
            	}
            	
            	// Drawing polyline in the Google Map for the i-th route
            	map.addPolyline(lineOptions);
            }
        }
    }
    
    private void gotoLocation (double latitude, double longitude, float zoom) {
    	
    	LatLng ll = new LatLng(latitude, longitude);
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
    	map.moveCamera(update);
    }
	
}
