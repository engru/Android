package com.oppo.transfer.ui.lib;

import com.oppo.transfer.ui.WindowNetworkSelect;
import com.oppo.transfer.utils.SystemUtil;
import com.oppo.transfer.utils.WifiUtil;
import com.wkey.develop.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

public class PopupWindowUtil {
	
	static PopupWindow pw = null;
	
	public PopupWindowUtil(){
		
	}
	
	public static void showWindow(Context mContext,View view){
		System.out.println("showwindow");
		if(pw == null){
		    TextView v = new TextView(mContext);
		    v.setText("sdfxxx");
		    v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//pw.dismiss();
					//pw =null;
				}
			});
		    //v.setBackgroundResource(R.color.gray);
		    System.out.println("Popup:sdx");
			pw = new PopupWindow(v,LayoutParams.MATCH_PARENT,  
                    350);
			
			pw.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
			
			//pw.setFocusable(true);
			//pw.setTouchable(true);
			pw.setOutsideTouchable(true);
			pw.setTouchInterceptor(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					System.out.println("poptouch:"+event.getAction());
					if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
						pw.dismiss();
						System.out.println("dismiss");
						pw = null;
						return true;
					}
					System.out.println("dismiss1");
					return false;
				}
			});
			pw.showAtLocation(view, Gravity.LEFT, 0, -SystemUtil.getHeight(mContext)/2+350);
		}
	}
	
	public static void show(Context mContext,View parent){
		
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		//自定义布局  
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(  
		                    R.layout.window_network_select_info, null, true); 
		showLocal(mContext,menuView,parent);
		
	}
	
	
	private static void showLocal(Context mContext,View layout,View parent){
		/*
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  
		//自定义布局  
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(  
		                    R.layout.window, null, true); 
		*/
		int Height = SystemUtil.getHeight(mContext)/2;
		pw = new PopupWindow(layout,LayoutParams.MATCH_PARENT, Height);//LayoutParams.WRAP_CONTENT);
		//pw.setWidth(SystemUtil.getWidth(mContext));
		
		
		pw.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
		
		//pw.setFocusable(true);
		WindowNetworkSelect wn = new WindowNetworkSelect();
		wn.init(mContext, layout);
		//initViews(mContext,layout);
		
		//pw.setAnimationStyle(R.style.AnimationPreview);	//动画
		
		pw.setOutsideTouchable(true);
		//pw.setTouchable(true);
		
		pw.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
					pw.dismiss();
					return true;
				}
				
				return false;
			}
		});
		pw.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, 0, SystemUtil.getStatusBarHeight(mContext)+parent.getHeight());//-SystemUtil.getHeight(mContext)/2+340+Height/2);
	}

	private static void initViews(Context mContext,View layout) {
		// TODO Auto-generated method stub
		//全局信息栏
		ImageView iv 	= (ImageView)layout.findViewById(R.id.network_type);
		TextView ssid 	= (TextView)layout.findViewById(R.id.network_ssid);
		TextView ip 	= (TextView)layout.findViewById(R.id.network_ip);
		TextView peer_ip= (TextView)layout.findViewById(R.id.network_peer_ip);
		
		//TabHost 栏
		TabHost th = (TabHost)layout.findViewById(android.R.id.tabhost);
		th.setup();
		th.addTab(th.newTabSpec("type").setIndicator("wifi direct").setContent(R.id.tab1));
		th.addTab(th.newTabSpec("type").setIndicator("hotspot").setContent(R.id.tab2));
		th.addTab(th.newTabSpec("type").setIndicator("wifi").setContent(R.id.tab3));
		
		//检测网络
		if(WifiUtil.checkNetwork(mContext)){
			System.out.println("network ok");
			//if(WifiUtil.isWifiWorking(mContext)){
			//	System.out.println("network wifi");
			//}else{
			//	
			//}
			System.out.println(WifiUtil.getNetworkType(mContext));
		}else {
			//wifi direct
			
		}
		
		//wifi Direct
		
		
		
		
		
		ip.setText(WifiUtil.getLocalIpAddress());
		peer_ip.setText(WifiUtil.getPeerIP(""));

	}

}
