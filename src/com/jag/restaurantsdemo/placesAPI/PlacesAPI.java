package com.jag.restaurantsdemo.placesAPI;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jag.restaurantsdemo.data.Place;
import com.jag.restaurantsdemo.utils.Information;

/** AsyncTask that calls the Google Places API and create the list of places from its response **/
public class PlacesAPI extends AsyncTask<String, Integer, List<Place>> {
	
	private Handler responseHandler;
	private Context context;
	//Log
	private String TAG = "DEMO";

	public PlacesAPI(Handler handler, Context context) {
		this.responseHandler = handler;
		this.context = context;
	}

	@Override
	protected List<Place> doInBackground(String... url) {
		String data = null;
		try {
			data = Information.callAPI(url[0]);
		    Information.savePlacesJSON(context, data);	
		} catch (Exception e) {
			data = Information.getPlacesJSON(context);
			Log.d(TAG, "JSON data empty? "+data);
		}

		List<Place> places = null;
		PlacesParser placeJsonParser = new PlacesParser();
		try {
			JSONObject jObject = new JSONObject(data);
			places = placeJsonParser.parse(jObject);
		} catch (Exception e) {
		}

		return places;
	}

	@Override
	protected void onPostExecute(List<Place> places) {
		Message msg = responseHandler.obtainMessage();
		msg.obj = places;
		msg.what = Information.UPDATED;
		responseHandler.sendMessage(msg);
	}

}