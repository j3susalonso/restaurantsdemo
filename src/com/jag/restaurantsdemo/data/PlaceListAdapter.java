package com.jag.restaurantsdemo.data;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jag.restaurantsdemo.R;

/** BaseAdapter used by PlacesListFragment **/
public class PlaceListAdapter extends BaseAdapter {

	private class ViewHolder {
		public TextView textViewName;
		public TextView textViewVicinity;
	}
	private List<Place> places;
	private LayoutInflater inflater;

	public PlaceListAdapter(Context context, List<Place> places) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.places = places;
	}

	@Override
	public int getCount() {
		if (places != null) {
			return places.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (places != null && position >= 0 && position < getCount()) {
			return places.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (places != null && position >= 0 && position < getCount()) {
			return places.get(position).id;
		}

		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {
			view = inflater.inflate(R.layout.item_place_list, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.textViewName = (TextView) view.findViewById(R.id.name);
			viewHolder.textViewVicinity = (TextView) view.findViewById(R.id.vicinity);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		Place locationModel = places.get(position);
		viewHolder.textViewName.setText(locationModel.name);
		viewHolder.textViewVicinity.setText(locationModel.vicinity);
		return view;
	}

}
