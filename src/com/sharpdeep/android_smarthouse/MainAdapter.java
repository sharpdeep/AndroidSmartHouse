package com.sharpdeep.android_smarthouse;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter{
	private Context context;
	private String[] name;
	private int[] resId;
	
	public MainAdapter(Context context,String[] name, int[] resId) {
		this.context = context;
		this.name = name;
		this.resId = resId;
	}

	@Override
	public int getCount() {
		return name.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.cell, null);
			holder.imageview = (ImageView)convertView.findViewById(R.id.imageview);
			holder.textview = (TextView)convertView.findViewById(R.id.textview);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.imageview.setImageResource(resId[position]);
		holder.textview.setText(name[position]);
		
		return convertView;
	}	

}

class ViewHolder{
	ImageView imageview;
	TextView textview;
}
