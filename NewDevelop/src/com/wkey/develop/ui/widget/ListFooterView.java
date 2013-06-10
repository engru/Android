package com.wkey.develop.ui.widget;

import com.wkey.develop.R;
import com.wkey.develop.ui.widget.DynamicListView.OnRefreshListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListFooterView extends LinearLayout {
	
	private LayoutInflater inflater;
	private LinearLayout footerView;
	
	private TextView moreTextView;
	private LinearLayout loadProgressBar;
	
	Context mContext;
	
	public ListFooterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(context);
		addView(footerView);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		inflater = LayoutInflater.from(context);
		footerView = (LinearLayout) inflater.inflate(R.layout.list_footer, null);
		
		moreTextView=(TextView)footerView.findViewById(R.id.more_id);
	    loadProgressBar=(LinearLayout)footerView.findViewById(R.id.load_id);
	    
		footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,//.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		
		
		moreTextView.setOnClickListener(mBtnListener);
	}

	private OnClickListener mBtnListener = new OnClickListener() {
		LayoutInflater flater; //= LayoutInflater.from(mContext);
		View view;
		@Override
		public void onClick(View v) {
			//隐藏"加载更多"
            moreTextView.setVisibility(View.GONE);
            //显示进度条
            loadProgressBar.setVisibility(View.VISIBLE);
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //休眠3秒，用于模拟网络操作时间
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    //加载模拟数据：下一页数据， 在正常情况下，上面的休眠是不需要，直接使用下面这句代码加载相关数据
                    //chageListView(data.size(),pageSize);
                    onRefresh();
                    Message mes=handler.obtainMessage(1);
                    handler.sendMessage(mes);
                }
            }).start();
		}
	};
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                //改变适配器的数目
                //adapter.count += pageSize;
                //通知适配器，发现改变操作
               // adapter.notifyDataSetChanged();
                
                //再次显示"加载更多"
                moreTextView.setVisibility(View.VISIBLE);
                //再次隐藏“进度条”
                loadProgressBar.setVisibility(View.GONE);
                break;

            default:
                break;
            }
            
            super.handleMessage(msg);
        }
    };


	private OnRefreshListener refreshListener;	
	public void setonRefreshListener(OnRefreshListener refreshListener) {
		// TODO Auto-generated method stub
		this.refreshListener = refreshListener;
	}
	
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh("footer");
		}
	}
}
