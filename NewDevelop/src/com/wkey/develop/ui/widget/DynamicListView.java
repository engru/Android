package com.wkey.develop.ui.widget;

import java.util.Date;

import com.wkey.develop.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DynamicListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";

	private ListHeaderView headView;
	private ListFooterView footerView;

	private int headContentWidth;
	private int headContentHeight;

	private int state;

	public DynamicListView(Context context) {
		super(context);
		init(context);
	}

	public DynamicListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	protected void init(Context context) {
		headView = new ListHeaderView(context);
		footerView = new ListFooterView(context);

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);

		addHeaderView(headView, null, false);
		addFooterView(footerView,null,false);
		setOnScrollListener(this);
	}
	
	public void setHeaderView(View view){
		if(view != null)
			addHeaderView(view,null,false);
		else 
			addHeaderView(headView,null,false);
	}
	public void setFooterView(View view){
		if(view != null)
			addFooterView(view,null,false);
		else 
			addFooterView(footerView,null,false);
		
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		//headView.setIndex(firstVisiableItem);
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		headView.onTouchEvent_p(event);
		//setSelection(0);
		
	//	switch (event.getAction()) {
	//		case MotionEvent.ACTION_DOWN:
				//headView.setPadding(0, 0, 0, 0);
				//setSelection(1);
	//	}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		headView.changeHeaderViewByState(state);
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		headView.setonRefreshListener(refreshListener);
		footerView.setonRefreshListener(refreshListener);
	}
///////////////////////////////////////
	public interface OnRefreshListener {
		public void onRefresh(String str);
	}

	public void onRefreshComplete() {
		state = 3;//DONE
		//lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
		headView.changeHeaderViewByState(state);
	}

	public void setAdapter(BaseAdapter adapter) {
		//lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
		super.setAdapter(adapter);
	}

}
