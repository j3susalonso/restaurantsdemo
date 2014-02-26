package com.jag.restaurantsdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jag.restaurantsdemo.data.Place;
import com.jag.restaurantsdemo.fragments.PlacesListFragment;
import com.jag.restaurantsdemo.fragments.PlacesMapFragment;
import com.jag.restaurantsdemo.location.UserLocation;
import com.jag.restaurantsdemo.utils.Information;

/** Main class. Handle the application fragments and initialize the UserLocation class **/
public class MainActivity extends FragmentActivity {

	// LocationListener to retrieve user's location
	private UserLocation userLocation;
	// Handler
	public Handler placesUpdate;
	// Progress
	private ProgressDialog mDialog;
	// Search again button
	private Button searchButton;
	//Log
	private String TAG = "DEMO";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		placesUpdate = createPlacesUpdateHandler();
		initViewElements();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(userLocation != null){
			userLocation.removeUpdates();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Is Connected: " + Information.isConnected(this));
		if (!Information.isConnected(this)) {
			showToast(Information.FAIL_MESSAGE);
		}
		// Start Location listener
		userLocation = new UserLocation(this, placesUpdate);
		userLocation.requestUpdates();
		mDialog.show();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(userLocation != null){
			userLocation.removeUpdates();
		}
	}
	
	private void initViewElements() {
		// Progress Dialog
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Please wait...");
		mDialog.setCancelable(false);
		// Search button
		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userLocation == null || !Information.isConnected(MainActivity.this)) {
					showToast(Information.FAIL_MESSAGE);
				} else {
					mDialog.show();
					userLocation.update();
				}
			}
		});
	}
	
	/** Creates the handler to update the fragments when the user's location is updated **/
	private Handler createPlacesUpdateHandler() {
		return new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case Information.UPDATED:
					@SuppressWarnings("unchecked")
					List<Place> places = (ArrayList<Place>) msg.obj;
					if (places != null) {
						updateFragments(places);
					} else {
						showToast(Information.FAIL_MESSAGE);
					}
					mDialog.dismiss();
					break;
				
				case Information.GPS_DISABLED:
					showToast(Information.GPS_MESSAGE);
					mDialog.dismiss();
					break;
				}
				return false;
			}
			
			/** Update fragments with the updated places **/
			private void updateFragments(List<Place> places) {
				FragmentManager manager = getSupportFragmentManager();
				PlacesListFragment lFrag = (PlacesListFragment) manager.findFragmentByTag(Information.TABS[0].getId());
				PlacesMapFragment mFrag = (PlacesMapFragment) manager.findFragmentByTag(Information.TABS[1].getId());
				lFrag.update(MainActivity.this, places);
				mFrag.updateMap(places, userLocation.getLatLng());
			}
		});

	}

	private void showToast(String msg) {
		Log.d(TAG, "Show toast: "+msg);
		Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
