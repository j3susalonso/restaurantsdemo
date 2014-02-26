package com.jag.restaurantsdemo.data;

import com.google.android.gms.maps.model.LatLng;

/** Class that represents a place, indicating its coordinates, name and availability **/
public class Place {
	
	public int id;
	public double latitude;
	public double longitude;
	public String vicinity;
	public String open;
	public String name;
	public LatLng latLng;

	public Place(int id, LatLng latLng, String name, String vicinity, String open) {
		this.id = id;
		this.latLng = latLng;
		this.name = name;
		this.vicinity = vicinity;
		this.open = open;
	}
}
