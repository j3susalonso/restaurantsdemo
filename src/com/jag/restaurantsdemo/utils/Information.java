package com.jag.restaurantsdemo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.jag.restaurantsdemo.R;
import com.jag.restaurantsdemo.fragments.PlacesListFragment;
import com.jag.restaurantsdemo.fragments.PlacesMapFragment;
import com.jag.restaurantsdemo.fragments.TabInfo;

/** Helper class containing final variables and some utils like Google Places API call **/
public class Information {
	//Fail message
	public static String FAIL_MESSAGE = "Please, check your internet connection and try again.";
	public static String GPS_MESSAGE = "Please, enable your GPS and try again.";
	
	//Min distance to update location
	public static long MIN_DISTANCE = 50;
	//Min time to update location
	public static long MIN_TIME = 5000;
	public static float MAP_ZOOM = 13;
	
	//Places availability
	public enum PlaceStatuses {
		OPEN, CLOSED, UNKNOWN
	}
	
	//Radius
	public final static int RADIUS = 1000;
	
	//Types
	public final static String TYPES = "restaurant";
	
	//API Key
	public final static String API_KEY = "GOOGLE API KEY";
	
	//Handler message what
	public final static int UPDATED = 0;
	public final static int GPS_DISABLED = 1;
	
	//Application TABS
	public static final TabInfo[] TABS = new TabInfo[] {
		new TabInfo(R.id.tabPlacesList, R.string.tab_places_list, new PlacesListFragment()),
		new TabInfo(R.id.tabPlacesMap,  R.string.tab_places_map, new PlacesMapFragment()), 
	};
	
	public static String buildAPIQuery(double latitude, double longitude) {
		StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		sb.append("location=" + latitude + "," + longitude);
		sb.append("&radius="+RADIUS);
		sb.append("&types="+TYPES);
		sb.append("&sensor=true");
		sb.append("&key="+API_KEY);
		return sb.toString();
	}

	public static String callAPI(String strUrl) throws Exception {	
		String data = "";
		URL url = new URL(strUrl);
		//URL Connection
		HttpURLConnection urlConnection = null;
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.connect();
		//InputStream
		InputStream inputStream = null;
		inputStream = urlConnection.getInputStream();
		//BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		//Retrieve data
		data = sb.toString();
		//Close
		br.close();
		inputStream.close();
		urlConnection.disconnect();
		return data;
	}
	
	public static String getPlacesJSON(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString("places_data", "");
	}
	
	public static void savePlacesJSON(Context context, String data) {
		SharedPreferences sharePreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor toEdit = sharePreferences.edit();
		toEdit.putString("places_data", data);
		toEdit.commit();
	}
	
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.getActiveNetworkInfo();
		}
		return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
	}
}
