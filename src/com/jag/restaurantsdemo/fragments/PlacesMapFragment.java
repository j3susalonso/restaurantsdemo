package com.jag.restaurantsdemo.fragments;

import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jag.restaurantsdemo.R;
import com.jag.restaurantsdemo.data.Place;
import com.jag.restaurantsdemo.utils.Information;

/** Fragment containing the GoogleMap **/
public class PlacesMapFragment extends Fragment {
	
	MapView mapView;
	GoogleMap map;
	private String TAG = "DEMO";

	private void addMarker(String name, String vicinity, String open, LatLng latLng) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title(name);
		markerOptions.snippet(vicinity);
		setMarkerColor(open, markerOptions);
		map.addMarker(markerOptions);
	}
	
	private void centerMap(LatLng latLng) {
		Log.d(TAG, "centerMap " + latLng.toString());
		map.clear();
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		map.animateCamera(CameraUpdateFactory.zoomTo(Information.MAP_ZOOM));
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map, container, false);
		mapView = (MapView) v.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		map.setMyLocationEnabled(true);
		try {
			MapsInitializer.initialize(this.getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	private void setMarkerColor(String open, MarkerOptions markerOptions) {
		switch (Information.PlaceStatuses.valueOf(open.toUpperCase(Locale.ENGLISH))) {
			case OPEN:
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				break;
			case CLOSED:
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				break;
			case UNKNOWN:
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
				break;
			default:
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
				break;
		}
	}

	public void updateMap(List<Place> places, LatLng latLng) {
		centerMap(latLng);
		for (int i = 0; i < places.size(); i++) {
			Place place = places.get(i);
			addMarker(place.name, place.vicinity, place.open, place.latLng);
		}
	}
}