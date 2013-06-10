package com.wkey.develop.ui;

import com.wkey.develop.R;
import com.wkey.develop.game.MySurfaceView;
import com.wkey.develop.game.f3d.f3dSurfaceView;
import com.wkey.develop.game.objloader.MyRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MyTestActivity extends Activity  {
	private MySurfaceView mSurfaceView;
	private f3dSurfaceView mGLSurfaceView;
	Context mContext;
	int i=0;
	Button mBtn_test;
	RelativeLayout view;
    protected void onCreate(Bundle savedInstanceState) {
    	mContext = this;
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.test);
        //this.getWindow().getDecorView().setOnClickListener(mBtnListener);
        mBtn_test = (Button) findViewById(R.id.button1);
        mBtn_test.setOnClickListener(mBtnListener);
        
        view = (RelativeLayout)findViewById(R.id.mView);

       
   }
    
    private OnClickListener mBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			System.out.println("MyTestActivity:onclick");
			i++;
	        if(i%3==1 || false){
	        	view.removeAllViews();
	           // mSurfaceView = new MySurfaceView(mContext);
	            mGLSurfaceView = new f3dSurfaceView(mContext);
	            mGLSurfaceView.requestFocus();//获取焦点
	            mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
		        view.addView(mGLSurfaceView);
	        }else if(i%3==2){
	        	view.removeAllViews();
		        mSurfaceView = new MySurfaceView(mContext);
	            mSurfaceView.requestFocus();//获取焦点
	            mSurfaceView.setFocusableInTouchMode(true);//设置为可触控
	            view.addView(mSurfaceView);
	        }
	        else{
	        	view.removeAllViews();
	        	//view.addView(new MyRenderer(mContext));
	        }
	       // mBtn_test.setVisibility(View.VISIBLE);
	        
		}
    };

}
