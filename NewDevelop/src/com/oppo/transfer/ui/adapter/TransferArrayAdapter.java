package com.oppo.transfer.ui.adapter;

import java.util.List;

import com.wkey.develop.R;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TransferArrayAdapter<T> extends ArrayAdapter<T> {
	float progress = 0;
	
	public TransferArrayAdapter(Context context, int resource,
			int textViewResourceId, List objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public View getView(int position, View convertView, ViewGroup parent){
		View v = super.getView(position, convertView, parent);
        TextView tv = (TextView)v.findViewById(R.id.background_progress);
        tv.setVisibility(View.VISIBLE);
        //tv.setWidth(300);//(int)((progress/100)*v.findViewById(R.id.container_framelayout).getWidth()));
        //System.out.println((int)((progress/100)*v.findViewById(R.id.container_framelayout).getWidth()));
        //System.out.println("getview "+position+" "+progress);
        
        tv.setLayoutParams(new FrameLayout.LayoutParams((int)((progress/100)*v.findViewById(R.id.container_framelayout).getWidth()), LayoutParams.MATCH_PARENT));
        return v;
		
	}
	
	
	public void setProgress(int state,int progress,int position){
		this.progress = (float)progress;
	}
	
	

}
