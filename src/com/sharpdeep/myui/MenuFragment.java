package com.sharpdeep.myui;

import com.sharpdeep.smarthouse.ui.MainActivity;
import com.sharpdeep.smarthouse.ui.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuFragment extends ListFragment{
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] menus = getResources().getStringArray(R.array.menu_item);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.menu_item,
				R.id.text01, menus);
		
		setListAdapter(adapter);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(getActivity() != null){
			if(getActivity() instanceof MainActivity){
				MainActivity ma = (MainActivity)getActivity();
				ma.onSlidingMenuItemClick(position);
			}
		}
	}

}
