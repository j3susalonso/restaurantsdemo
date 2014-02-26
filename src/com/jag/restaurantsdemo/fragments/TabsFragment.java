package com.jag.restaurantsdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.jag.restaurantsdemo.R;
import com.jag.restaurantsdemo.utils.Information;

public class TabsFragment extends Fragment implements OnTabChangeListener {

	private View rootView;
	private TabHost tabHost;
	private FragmentManager manager;

	private TabSpec createTab(LayoutInflater inflater, TabHost tabHost,	View root, TabInfo tabDefinition) {
		ViewGroup tabsView = (ViewGroup) root.findViewById(android.R.id.tabs);
		View tabView = tabDefinition.createTabView(inflater, tabsView);
		TabSpec tabSpec = tabHost.newTabSpec(tabDefinition.getId());
		tabSpec.setIndicator(tabView);
		tabSpec.setContent(tabDefinition.getTabViewId());
		return tabSpec;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		tabHost.setOnTabChangedListener(this);
		manager = getFragmentManager();
		if (Information.TABS.length > 0) {
			onTabChanged(Information.TABS[0].getId());
			onTabChanged(Information.TABS[1].getId());
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tabhost, null);
		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();
		for (TabInfo tab : Information.TABS) {
			TabSpec tabSpec = createTab(inflater, tabHost, rootView, tab);
			tabHost.addTab(tabSpec);
		}
		return rootView;
	}

	@Override
	public void onTabChanged(String tabId) {
		for (TabInfo tab : Information.TABS) {
			if (tabId != tab.getId() || manager.findFragmentByTag(tabId) != null) continue;
			manager.beginTransaction().replace(tab.getTabViewId(), tab.getFragment(), tabId).commit();
			return;
		}
	}
}
