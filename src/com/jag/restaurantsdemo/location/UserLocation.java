package com.jag.restaurantsdemo.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jag.restaurantsdemo.placesAPI.PlacesAPI;
import com.jag.restaurantsdemo.utils.Information;

/** This class retrieves the user's location and update the list of places using the PlacesAPI async class **/
public class UserLocation implements LocationListener {

	private LocationManager locationManager;
	private String provider;
	private double latitude;
	private double longitude;
	private Handler handler;
	private Context context;
	private LatLng latLng;
	private String TAG = "DEMO";
	private boolean providerEnabled;

	public UserLocation(Context context, Handler handler) {
		this.handler = handler;
		this.context = context;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			Log.d(TAG , "Provider " + provider + " selected.");
			providerEnabled = true;
			onLocationChanged(location);
		} else {
			latitude  = -1;
			longitude = -1;
			latLng = new LatLng(latitude, longitude);
		}
	}

	public double getLatitude() {
		return latitude;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public double getLongitude() {
		return longitude;
	}

	public void update() {
		if(providerEnabled){
			Location location = locationManager.getLastKnownLocation(provider);
			onLocationChanged(location);
		}
		else{
			handler.sendEmptyMessage(Information.GPS_DISABLED);
		}
	}

	@Override
	public void onLocationChanged(Location location) {	
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		latLng = new LatLng(latitude, longitude);
		PlacesAPI placesAPITask = new PlacesAPI(handler, context);
		placesAPITask.execute(Information.buildAPIQuery(latitude,longitude));
		Log.d(TAG, "onLocationChanged " + latitude + " " + longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "Disabled provider " + provider);
		providerEnabled = false;
		handler.sendEmptyMessage(Information.GPS_DISABLED);
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "Enabled provider " + provider);
		providerEnabled = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void removeUpdates() {
		locationManager.removeUpdates(this);
	}

	public void requestUpdates() {
		locationManager.requestLocationUpdates(provider, Information.MIN_TIME, Information.MIN_DISTANCE, this);
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}