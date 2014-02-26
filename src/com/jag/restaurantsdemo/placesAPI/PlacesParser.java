package com.jag.restaurantsdemo.placesAPI;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.jag.restaurantsdemo.data.Place;

public class PlacesParser {

	/** Creates the Places list from the JSONObject received from the Google Places API**/
	public List<Place> parse(JSONObject jObject) {
		JSONArray jPlaces = null;
		//If the response doesn't contains results, return null
		try {
			jPlaces = jObject.getJSONArray("results");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//Create list of Place objects
		List<Place> places = new ArrayList<Place>();
		for (int i = 0; i < jPlaces.length(); i++) {
			JSONObject jSONPlace;
			try {
				jSONPlace = (JSONObject) jPlaces.get(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Place place = createPlace(i, jSONPlace);
			if(place!=null){
				places.add(place);
			}
		}
		return places;
	}
	
	/** Creates a Place object **/
	private Place createPlace(int id, JSONObject jSONPlace) {
		String name = "";
		String vicinity = "";
		String status = "unknown";
		double latitude = 0.0;
		double longitude = 0.0;
		LatLng latLng= null;
		
		try {
			name = jSONPlace.getString("name");
			vicinity = jSONPlace.getString("vicinity");
			latitude = Double.parseDouble(jSONPlace.getJSONObject("geometry").getJSONObject("location").getString("lat"));
			longitude = Double.parseDouble(jSONPlace.getJSONObject("geometry").getJSONObject("location").getString("lng"));
			latLng = new LatLng(latitude, longitude);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		//If opening_hours not declared, keep status as unknown
		try {
			String open = jSONPlace.getJSONObject("opening_hours").getString("open_now");
			status = open.contains("true") ? "open" : "closed";
		} catch (JSONException e) {
		}
		//Return the create Place from the JSONObject
		return new Place(id, latLng, name, vicinity, status);
	}
}
