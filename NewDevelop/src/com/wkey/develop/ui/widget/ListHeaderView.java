package com.wkey.develop.ui.widget;

import com.wkey.develop.R;
import com.wkey.develop.ui.widget.DynamicListView.OnRefreshListener;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListHeaderView extends LinearLayout{
	private static final String TAG = "headerView";
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	
	private LayoutInflater inflater;
	
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;


	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	//private boolean isBack=true;
	private int state;
	private int headContentWidth;
	private int headContentHeight;
	
	public ListHeaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
		addView(headView);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.list_head, null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);
		

		headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		//measureView(this);
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		this.setPadding(0, -1 * headContentHeight, 0, 0);
		this.invalidate();
		
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
		
		state = DONE;

		isRefreshable = false;
	}
	
	
	private boolean isBack;

	public void changeHeaderViewByState(int state) {
		this.state = state;
		changeHeaderViewByState();
		
	}
	
	// ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
	public void changeHeaderViewByState(){//int state,boolean isBack) {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("�ɿ�ˢ��");

			Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("����ˢ��");
			} else {
				tipsTextview.setText("����ˢ��");
			}
			Log.v(TAG, "��ǰ״̬������ˢ��");
			break;

		case REFRESHING:

			this.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("����ˢ��...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "��ǰ״̬,����ˢ��...");
			break;
		case DONE:
			this.setPadding(0, -1 * headContentHeight, 0, 0);
			//headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			//arrowImageView.setImageResource(R.drawable.arrow);
			tipsTextview.setText("����ˢ��");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "��ǰ״̬��done");
			break;
		}
	}
	
	
	// �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	/*
	private View main;
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
	*/
	
	
	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean isRecored;
	private int startY;
	private int firstItemIndex;
	// ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���
	private final static int RATIO = 3;
	
	public void onTouchEvent_p(MotionEvent event) {
		if (isRefreshable) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstItemIndex == 0 && !isRecored) {
				isRecored = true;
				startY = (int) event.getY();
				Log.v(TAG, "��downʱ���¼��ǰλ�á�");
			}
			//changeHeaderViewByState();
			break;

		case MotionEvent.ACTION_UP:

			if (state != REFRESHING && state != LOADING) {
				if (state == DONE) {
					// ʲô������
				}
				if (state == PULL_To_REFRESH) {
					state = DONE;
					changeHeaderViewByState();

					Log.v(TAG, "������ˢ��״̬����done״̬");
				}
				if (state == RELEASE_To_REFRESH) {
					state = REFRESHING;
					changeHeaderViewByState();
					onRefresh();

					Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");
				}
			}

			isRecored = false;
			isBack = false;

			break;

		case MotionEvent.ACTION_MOVE:
			int tempY = (int) event.getY();

			if (!isRecored && firstItemIndex == 0) {
				Log.v(TAG, "��moveʱ���¼��λ��");
				isRecored = true;
				startY = tempY;
			}

			if (state != REFRESHING && isRecored && state != LOADING) {
				// ��֤������padding�Ĺ����У���ǰ��λ��һֱ����head������������б�����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���
				// ��������ȥˢ����
				if (state == RELEASE_To_REFRESH) {
					//setSelection(0);
					// �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�
					if (((tempY - startY) / RATIO < headContentHeight)
							&& (tempY - startY) > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();

						Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");
					}
					// һ�����Ƶ�����
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();
						Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");
					}
					// �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
					else {
						// ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������
					}
				}
				// ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬
				if (state == PULL_To_REFRESH) {
					//setSelection(0);
					// ���������Խ���RELEASE_TO_REFRESH��״̬
					if ((tempY - startY) / RATIO >= headContentHeight) {
						state = RELEASE_To_REFRESH;
						isBack = true;
						changeHeaderViewByState();

						Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");
					}
					// ���Ƶ�����
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();

						Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");
					}
				}

				// done״̬��
				if (state == DONE) {
					if (tempY - startY > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
					}
				}

				// ����headView��size
				if (state == PULL_To_REFRESH) {
					this.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
					//headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
				}

				// ����headView��paddingTop
				if (state == RELEASE_To_REFRESH) {
					this.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
					//headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
				}
			}
			break;
		}
		}
	}
	
	private OnRefreshListener refreshListener;
	private boolean isRefreshable;
	
	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}
	
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh("head");
		}
	}

	public void setIndex(int firstVisiableItem) {
		// TODO Auto-generated method stub
		this.firstItemIndex = firstVisiableItem;
	}

}
