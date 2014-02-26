package com.jag.restaurantsdemo.fragments;

import java.util.UUID;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jag.restaurantsdemo.R;

/**
 * A class that defines a UI tab.
 */
public class TabInfo {

	private final int tabTitleId;
	private final int tabViewId;
	private final String tabUuid;
	private final Fragment fragment;

	public TabInfo(int tabViewId, int tabTitleId, Fragment fragment) {
		this.tabViewId = tabViewId;
		this.tabTitleId = tabTitleId;
		this.tabUuid = UUID.randomUUID().toString();
		this.fragment = fragment;
	}

	 public View createTabView(LayoutInflater inflater, ViewGroup tabsView) {
		View indicator = inflater.inflate(R.layout.tab, tabsView, false);
		initTitleView(indicator);
		setLayoutParams(indicator);
		return indicator;
	}

	 public Fragment getFragment() {
		return fragment;
	}
	 
	public String getId() {
	   return tabUuid;
	 }

	public int getTabViewId() {
	   return tabViewId;
	 }

	private void initTitleView(View indicator) {
		TextView titleView = (TextView) indicator.findViewById(R.id.tabTitle);
		titleView.setText(tabTitleId);
		titleView.setGravity(Gravity.CENTER);
	}

	private void setLayoutParams(View indicator) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		indicator.setLayoutParams(layoutParams);
	}
}