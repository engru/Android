package com.wkey.develop.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;

import com.wkey.develop.MainActivity;
import com.wkey.develop.R;

public class NetworkActivity extends ViewGroup {
	Context mContext;
	//LocalActivityManager localActivityManager;
	
	public NetworkActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//localActivityManager = new LocalActivityManager(this, false);//getLocalActivityManager(); 
		View network = LayoutInflater.from(context).inflate(R.layout.activity_network, null);
		addView(network);
		mContext =context;
		dm = new DisplayMetrics();
		       InitWidth();
		        InitTextView();
		        InitViewPager();
	}
	
	public NetworkActivity(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View network = LayoutInflater.from(context).inflate(R.layout.activity_network, null);
		addView(network);
		mContext =context;
		dm = new DisplayMetrics();
		       InitWidth();
		        InitTextView();
		        InitViewPager();
	}


	private TabHost mTabHost;
	
	private ViewPager pager;
	private TextView tvTabActivity, tvTabGroups, tvTabFriends, tvTabChat;
	View view1,view2,view3,view4;
	DisplayMetrics dm;
	
	private List<View> views;
	
 //   protected void onCreate(Bundle savedInstanceState) {
  //      super.onCreate(savedInstanceState);
        //mContext = context;
        
  //      dm = new DisplayMetrics();
       // int screenW = dm.widthPixels;
        
  //      setContentView(R.layout.activity_network);
        
 //       InitWidth();
 //       InitTextView();
 //       InitViewPager();
        
        /*
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        
		mTabHost.addTab(mTabHost
				.newTabSpec("tab0")
				.setIndicator("tab0")
				.setContent(R.id.tab1));
        
		mTabHost.addTab(mTabHost
				.newTabSpec("tab1")
				.setIndicator("tab1")
				.setContent(R.id.tab2));
		mTabHost.addTab(mTabHost
				.newTabSpec("tab2")
				.setIndicator("tab2")
				.setContent(R.id.tab3));
		mTabHost.setCurrentTab(1);
		*/
        
//    }
    
    
    ImageView ivBottomLine;
    int bottomLineWidth;
    int offset = 0 ;
    int position_one,position_two,position_three;
    
    private void InitWidth() {
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
       // Log.d(TAG, "cursor imageview width=" + bottomLineWidth);
        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = 1080;//dm.widthPixels;
        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
        //Log.i("MainActivity", "offset=" + offset);
//
        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }
    
    private void InitTextView() {
        tvTabActivity = (TextView) findViewById(R.id.tv_tab_activity);
        tvTabGroups = (TextView) findViewById(R.id.tv_tab_groups);
        tvTabFriends = (TextView) findViewById(R.id.tv_tab_friends);
        tvTabChat = (TextView) findViewById(R.id.tv_tab_chat);

        tvTabActivity.setOnClickListener(new MyOnClickListener(0));
        tvTabGroups.setOnClickListener(new MyOnClickListener(1));
        tvTabFriends.setOnClickListener(new MyOnClickListener(2));
        tvTabChat.setOnClickListener(new MyOnClickListener(3));
    }
    
    private void InitViewPager() {
    	/*
        pager = (ViewPager) findViewById(R.id.vp_contains);
        fragmentsList = new ArrayList<Fragment>();
        LayoutInflater mInflater = getLayoutInflater();
        View activityView = mInflater.inflate(R.layout.lay1, null);

        Fragment activityfragment = TestFragment.newInstance("Hello Activity.");
        Fragment groupFragment = TestFragment.newInstance("Hello Group.");
        Fragment friendsFragment=TestFragment.newInstance("Hello Friends.");
        Fragment chatFragment=TestFragment.newInstance("Hello Chat.");

        fragmentsList.add(activityfragment);
        fragmentsList.add(groupFragment);
        fragmentsList.add(friendsFragment);
        fragmentsList.add(chatFragment);
        
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
        */
        

        pager = (ViewPager) findViewById(R.id.vp_contains);
        
        
       // LayoutInflater lf = getLayoutInflater().from(this);
        LayoutInflater lf =LayoutInflater.from(mContext);
        //view1 = findViewById(R.id.tab1);
       // view2 = findViewById(R.id.tab2);
        //view3 = findViewById(R.id.tab3);
        view1 = lf.inflate(R.layout.network_viewpager_info, null);
        view2 = lf.inflate(R.layout.plugin_framework_main, null);
        view3 = lf.inflate(R.layout.menu, null);
        view4 = lf.inflate(R.layout.menu, null);
        
        
        views=new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
        pager.setAdapter(new MyViewPagerAdapter(views));
     //   pager.setOnTouchListener(mTouchListener);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
        pager.setCurrentItem(2);

    }
    
    private OnTouchListener mTouchListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO Auto-generated method stub
			boolean bl = true;// = pager.onTouchEvent(event);
			System.out.println(bl + " askdjfaklsdfkasjdkf");
			
	        final float x = event.getX();
	        final float y = event.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{   mLastMotionX = x;
				mLastMotionY = y;
				//bl = true;
			}
			else if (event.getAction() == MotionEvent.ACTION_UP)
			{
				//bl = false;
			}
			else if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				int deltaX = (int) (mLastMotionX - x);
	            if(currIndex == 0)
	            	if(deltaX<0)
	            		;
	            		//bl = false;
				//bl=  true;
			}
	
			return false;
			//return bl?pager.onTouchEvent(event):false;
		}
    	
    };
    
    
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
        	System.out.println("TextView onclick:"+index);
            index = i;
        }

        @Override
        public void onClick(View v) {
        	System.out.println("TextView onclick:"+index);
            pager.setCurrentItem(index);
        }
    };
    
    
    public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
          
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        }  
  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));  
        }  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {            
             container.addView(mListViews.get(position), 0);  
             return mListViews.get(position);  
        }  

  
        @Override  
        public int getCount() {           
            return  mListViews.size();  
        }  
          
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1;  
        }  
    }  

    public int currIndex = 0;
    public class MyOnPageChangeListener implements OnPageChangeListener{  
  
        int one = dm.widthPixels/3;// 页卡1 -> 页卡2 偏移量  
        int two = one * 2;// 页卡1 -> 页卡3 偏移量  
        public void onPageScrollStateChanged(int arg0) {  
              
              
        }  
  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
              
              
        }  
  /*
        public void onPageSelected(int arg0) {  
            /两种方法，这个是一种，下面还有一种，显然这个比较麻烦 
            Animation animation = null; 
            switch (arg0) { 
            case 0: 
                if (currIndex == 1) { 
                    animation = new TranslateAnimation(one, 0, 0, 0); 
                } else if (currIndex == 2) { 
                    animation = new TranslateAnimation(two, 0, 0, 0); 
                } 
                break; 
            case 1: 
                if (currIndex == 0) { 
                    animation = new TranslateAnimation(offset, one, 0, 0); 
                } else if (currIndex == 2) { 
                    animation = new TranslateAnimation(two, one, 0, 0); 
                } 
                break; 
            case 2: 
                if (currIndex == 0) { 
                    animation = new TranslateAnimation(offset, two, 0, 0); 
                } else if (currIndex == 1) { 
                    animation = new TranslateAnimation(one, two, 0, 0); 
                }  
                break; 
                 
            } 
            /  
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。  
            currIndex = arg0;  
            animation.setFillAfter(true);// True:图片停在动画结束位置  
            animation.setDuration(300);  
            imageView.startAnimation(animation);  
            //Toast.makeText(WeiBoActivity.this, "您选择了"+ viewPager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();  
        }  
        */

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
           //         tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, 0, 0, 0);
            //        tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, 0, 0, 0);
          //          tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                }
          //      tvTabActivity.setTextColor(resources.getColor(R.color.white));
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
           //         tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
           //         tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_one, 0, 0);
           //         tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                }
          //      tvTabGroups.setTextColor(resources.getColor(R.color.white));
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_two, 0, 0);
           //         tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
           //         tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_two, 0, 0);
           //         tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                }
            //    tvTabFriends.setTextColor(resources.getColor(R.color.white));
                break;
            case 3:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_three, 0, 0);
             //       tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_three, 0, 0);
              //      tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_three, 0, 0);
                   // tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                }
                //tvTabChat.setTextColor(resources.getColor(R.color.white));
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }
          
    }
    
    private final static int VIEW_MARGIN=2;
    private View main;
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
        main = getChildAt(0);// 
        main.setVisibility(VISIBLE);
        //  
        main.measure(
            MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        main.layout(0, 0, getWidth(), getHeight());
	}  
	
    private float mLastMotionX;
    private float mLastMotionY;
    
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
       // System.out.println(ev.toString());//"deltax:"+action);
        
     //   if(currIndex == 0)
        	//if(deltaX<0)
     //   		return true;
        switch (action) {

	        case MotionEvent.ACTION_DOWN :
	            mLastMotionX = x;
	            mLastMotionY = y;
	            if(currIndex == 0)
	            	//if(deltaX<0)
	            	//	return true;
	            return false;
	          
	        case MotionEvent.ACTION_MOVE :
	            int deltaX = (int) (mLastMotionX - x);
	            mLastMotionX = x;
	            mLastMotionY = y;
	            System.out.println("deltax:"+deltaX);
	            if(currIndex == 0)
	            	if(deltaX<=0)
	            		//return true;
	            return false;
        
	        case MotionEvent.ACTION_UP :
	            return false;
	        case MotionEvent.ACTION_CANCEL :
	            return false;
	        default :
	        	return false;
        }
    }
    
    
    public boolean onTouchEvent(MotionEvent event) {  

       // return super.onTouchEvent(event);  
        boolean bl = true;//super.onTouchEvent(event);
    	
        int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        System.out.println(event.toString());
        switch (action & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN :
	            mLastMotionX = x;
	            mLastMotionY = y;
	            break;
	        case MotionEvent.ACTION_MOVE :
	            int deltaX = (int) (mLastMotionX - x);
	            mLastMotionX = x;
	            mLastMotionY = y;
	            System.out.println(deltaX+"  " +currIndex);
	            if(currIndex == 0)
	            	if(deltaX<=0)
	            		break;
	            
	            break;
	        case MotionEvent.ACTION_UP :
	        	break;
	        case MotionEvent.ACTION_CANCEL :
	        	break;

        }
        System.out.println(action+" "+ (bl?"true":"false"));
        return false;  
     //   return false;
    }
} 


