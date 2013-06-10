package com.wkey.develop.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.wkey.develop.R;
import com.wkey.develop.ui.widget.DynamicListView;
import com.wkey.develop.ui.widget.DynamicListView.OnRefreshListener;
import com.wkey.develop.ui.widget.adapter.ListViewAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NetworkInfoActivity extends Activity {
	DynamicListView mlistView;
    private ListViewAdapter listViewAdapter;
    private List<Map<String, Object>> listItems;
	LayoutInflater lf;
	Button btn_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   // lf =LayoutInflater.from(this);
	    setContentView(R.layout.network_viewpager_info);
	    
	    listItems = getListItems();   
        listViewAdapter = new ListViewAdapter(this, listItems); //创建适配器   
	 
	    mlistView = (DynamicListView) findViewById(R.id.mdylistView);
		mlistView.setAdapter(listViewAdapter);
		//mlistView.setOnScrollListener(mScrollListener);
	    //setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,COUNTRIES));
		
		btn_list = (Button)findViewById(R.id.btn_network_listview);
		btn_list.setOnClickListener(mBtnListener);
		

		mlistView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh(String str) {
				if(str.equals("head"))	
					new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							//data.add("刷新后添加的内容");
							return null;
						}
	
						@Override
						protected void onPostExecute(Void result) {
							listViewAdapter.notifyDataSetChanged();
							mlistView.onRefreshComplete();
						}
	
					}.execute();//.execute(null);
				else 
					refreshHandler.sendEmptyMessageDelayed(200, 0);
			}
		});
		
		
	}
	
	
	
	OnScrollListener mScrollListener = new OnScrollListener(){

		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				//LogUtility.debugForImage("touch scroll");
				//isScrolling = true;
				System.out.println("touch");
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				//LogUtility.debugForImage("fling");
				//isScrolling = true;
				System.out.println("fling");
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				//LogUtility.debugForImage("idle");
				//isScrolling = false;
				//doSthAfterScrollStoped();
				System.out.println("idle");
				break;
			}
			//if (listViewAdapter != null) {// 鎵€鏈夌晫闈㈤兘鏈夛紝@see listViewAdapter寮曠敤
			//	listViewAdapter.setScrolling(isScrolling);
			//}
		}
		
	};
	
	
	
	private OnClickListener mBtnListener = new OnClickListener() {
		LayoutInflater flater; //= LayoutInflater.from(mContext);
		View view;
		@Override
		public void onClick(View v) {
			System.out.println("zzzzzz");
			//List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();  
            Map<String, Object> map = new HashMap<String, Object>();    
            map.put("image", R.drawable.list_button_download_default);//imgeIDs[i]);               //图片资源   
            map.put("title", "物品名称：");              //物品标题   
            map.put("info", "wkey");     //物品名称   
            map.put("detail", "wkeycn"); //物品详情   
            listItems.add(map);  
			
			if(false){		//主线程中直接刷新，将会卡2s
				try
				{
				 Thread.currentThread().sleep(2000);//毫秒 
				}
				catch(Exception e){}
				listViewAdapter.notifyDataSetChanged();
			}
			else 
				refreshHandler.sendEmptyMessageDelayed(100, 0);//.sendEmptyMessage(100);
			listViewAdapter.addListItem(listItems);
		}
	};
	
	Handler refreshHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			
			// 闂笅浜己锛屽仛杩欎簺鍒ゆ柇鐨勫師鍥?
			switch (msg.what) {
			case 100://WHAT_NOTIFY_GET_PRODUCTS:
				updateListView();
				break;
			case 200:
				onRefresh();
				break;
			default:
				//if (isScrolling) {
					//hasNotify = true;
				//} else {
					//hasNotify = false;
					updateListView();
				//}
				break;
			}
		}
		
		private void updateListView() {
			listViewAdapter.notifyDataSetChanged();
		}
	};
	
    private String[] goodsNames = {"蛋糕"};//, "礼物", "邮票", "爱心", "鼠标", "音乐CD"};   
    private String[] goodsDetails = {   
            "蛋糕：好好吃。",    
            "礼物：礼轻情重。",    
            "邮票：环游世界。",    
            "爱心：世界都有爱。",   
            "鼠标：反应敏捷。",   
            "音乐CD：酷我音乐。"};  
    /**  
     * 初始化商品信息  
     */  
    private List<Map<String, Object>> getListItems() {   
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();   
        for(int i = 0; i < goodsNames.length; i++) {   
            Map<String, Object> map = new HashMap<String, Object>();    
            map.put("image", R.drawable.list_button_download_default);//imgeIDs[i]);               //图片资源   
            map.put("title", "物品名称：");              //物品标题   
            map.put("info", goodsNames[i%6]);     //物品名称   
            map.put("detail", goodsDetails[i%6]); //物品详情   
            listItems.add(map);   
        }      
        return listItems;   
    }   

	
    
    public void onRefresh(){
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();  
        Map<String, Object> map = new HashMap<String, Object>();    
        map.put("image", R.drawable.list_button_download_default);//imgeIDs[i]);               //图片资源   
        map.put("title", "物品名称：");              //物品标题   
        map.put("info", "footer");     //物品名称   
        map.put("detail", "footerView"); //物品详情   
        listItems.add(map);  

		refreshHandler.sendEmptyMessageDelayed(100, 0);//.sendEmptyMessage(100);
		listViewAdapter.addListItem(listItems);
    }
    
}
