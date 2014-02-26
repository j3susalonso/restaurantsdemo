package com.jag.restaurantsdemo.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jag.restaurantsdemo.data.Place;
import com.jag.restaurantsdemo.data.PlaceListAdapter;

/** Fragment containing the Places list **/
public class PlacesListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Context context = getActivity().getApplicationContext();
		if (context != null) {
			update(context, null);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	public void update(Context context, List<Place> places) {
		ListAdapter listAdapter = new PlaceListAdapter(context, places);
		setListAdapter(listAdapter);
	}

}
