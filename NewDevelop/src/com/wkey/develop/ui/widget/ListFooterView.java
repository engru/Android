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
			//����"���ظ���"
            moreTextView.setVisibility(View.GONE);
            //��ʾ������
            loadProgressBar.setVisibility(View.VISIBLE);
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //����3�룬����ģ���������ʱ��
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    //����ģ�����ݣ���һҳ���ݣ� ����������£�����������ǲ���Ҫ��ֱ��ʹ����������������������
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
                //�ı�����������Ŀ
                //adapter.count += pageSize;
                //֪ͨ�����������ָı����
               // adapter.notifyDataSetChanged();
                
                //�ٴ���ʾ"���ظ���"
                moreTextView.setVisibility(View.VISIBLE);
                //�ٴ����ء���������
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
