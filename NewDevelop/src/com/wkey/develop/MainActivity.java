package com.wkey.develop;


import com.oppo.transfer.ui.WifiTransferActivity;
import com.wkey.develop.ui.MyTestActivity;
import com.wkey.develop.ui.NetworkActivity;
import com.wkey.develop.ui.NetworkInfoActivity;
import com.wkey.develop.ui.WifiActivity;
import com.wkey.develop.ui.widget.FlipperView;
import com.wkey.develop.utils.PluginFrameworkActivity;

import android.os.Bundle;
import android.app.Activity; 
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent; 
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends ActivityGroup {

	private LinearLayout root;
	public FlipperView flipper;
	private Button mToggleBtn;
	private ImageButton mMenuBtn;
	///////
	private Button btn_network;
	private Button btn_wifi;
	private Button btn_bluetooth;
	private Button btn_nfc;
	private Button  btn_plugin;
	private RelativeLayout acti_root;
	private Context mContext;
	TabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar); 	//设置自定义的标题栏 
        //getLocalActivityManager();
        initViews();
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    private void initViews() {
		// TODO Auto-generated method stub
    	DisplayMetrics metric = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metric);
    	
    	View menu = LayoutInflater.from(this).inflate(R.layout.menu, null);
    	View content = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
    	flipper = (FlipperView) findViewById(R.id.flipper);
    	//flipper.addView(content);
		Intent it = new Intent(MainActivity.this, WifiTransferActivity.class);
		flipper.addView(getLocalActivityManager().startActivity("wifitransfer",it).getDecorView());
		
    	
    	root = (LinearLayout) findViewById(R.id.root);
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                (int) (metric.widthPixels * 0.75), LayoutParams.FILL_PARENT);
        root.addView(menu, rootParams);
        
      //  mToggleBtn = (Button) findViewById(R.id.Button);
      //  mToggleBtn.setOnClickListener(mBtnListener);
        
        mMenuBtn = (ImageButton) findViewById(R.id.btn_menu);
        if(mMenuBtn!=null)
        mMenuBtn.setOnClickListener(mBtnListener);
        
        ///////
        btn_network = (Button) findViewById(R.id.btn_network);
        btn_network.setOnClickListener(mBtnListener);
        
        btn_wifi = (Button) findViewById(R.id.btn_wifi);
        btn_wifi.setOnClickListener(mBtnListener);
        
        btn_bluetooth = (Button) findViewById(R.id.btn_bluetooth);
        btn_bluetooth.setOnClickListener(mBtnListener);
        
        btn_nfc = (Button) findViewById(R.id.btn_nfc);
        btn_nfc.setOnClickListener(mBtnListener);
        
        btn_plugin = (Button) findViewById(R.id.btn_plugin);
        btn_plugin.setOnClickListener(mBtnListener);
        
	}

    private OnClickListener mBtnListener = new OnClickListener() {
		LayoutInflater flater; //= LayoutInflater.from(mContext);
		Intent it;
		View view;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//flipper.removeAllViews();
			if(acti_root==null)
			{	flipper.removeAllViews();
				View content = LayoutInflater.from(mContext).inflate(R.layout.activity_welcome, null);
				flipper.addView(content);
			}
			
			System.out.println("onclick:"+v.getId());
			switch(v.getId()){
			case R.id.btn_menu:
				flipper.slidingMenu();
				//TextView tv = (TextView)findViewById(R.id.sliding_menu);
				//tv.setText("wkey change test");
				System.out.println("onClink");
				break;
			case R.id.btn_network:
			//	Intent it = new Intent(MainActivity.this, NetworkActivity.class);
			//	startActivity(it);
				
				acti_root =  (RelativeLayout) findViewById(R.id.acti_root);
				acti_root.removeAllViews();
				
				flater = LayoutInflater.from(mContext);
				view = flater.inflate(R.layout.item_network, null);
				//View network = view.findViewById(R.id.network_acti_1);//LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_network, null);
				//NetworkActivity  network = new NetworkActivity();
				acti_root.addView(view);

				flipper.slidingMenu();
				break;
			case R.id.btn_wifi:
				acti_root =  (RelativeLayout) findViewById(R.id.acti_root);
				acti_root.removeAllViews();
				
				it = new Intent(MainActivity.this, WifiActivity.class);
				acti_root.addView(getLocalActivityManager().startActivity("wifi",it).getDecorView());
				
				flipper.slidingMenu();
				break;
			case R.id.btn_bluetooth:
				acti_root =  (RelativeLayout) findViewById(R.id.acti_root);
				acti_root.removeAllViews();
				
				it = new Intent(MainActivity.this, MyTestActivity.class);
				acti_root.addView(getLocalActivityManager().startActivity("bt",it).getDecorView());
				
				flipper.slidingMenu();
				break;
			case R.id.btn_nfc:
				acti_root =  (RelativeLayout) findViewById(R.id.acti_root);
				acti_root.removeAllViews();
					it = new Intent(MainActivity.this, NetworkInfoActivity.class);
					//startActivity(it);
					View vn = getLocalActivityManager().startActivity("nfc",it).getDecorView();
					acti_root.addView(vn);
					flipper.slidingMenu();
				break;
			case R.id.btn_plugin:
				acti_root =  (RelativeLayout) findViewById(R.id.acti_root);
				acti_root.removeAllViews();
				
				flater = LayoutInflater.from(mContext);
				 view = flater.inflate(R.layout.plugin_framework_main, null);
				//System.out.println("asdjfajskdfjaklsjdfxxxx");
				acti_root.addView(view);

				flipper.slidingMenu();
				break;

			}
			
	        mMenuBtn = (ImageButton) findViewById(R.id.btn_menu);
	        if(mMenuBtn!=null)
	        mMenuBtn.setOnClickListener(mBtnListener);
		}
    };

	@Override
  //  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
  //      getMenuInflater().inflate(R.menu.main, menu);
  //      return true;
  //  }

 //   public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
 //       System.out.println("Main11:"+action);
 //       return true;//super.onInterceptTouchEvent(ev);  
 //   }
    
    
    public boolean onTouchEvent(MotionEvent event) {  


        int action = event.getAction();
        System.out.println("Main:"+action);
        return super.onTouchEvent(event);  
    }
}
