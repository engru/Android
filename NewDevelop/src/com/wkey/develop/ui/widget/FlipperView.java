package com.wkey.develop.ui.widget;


//import com.nearme.note.syncronize.SyncronizeService;

import com.wkey.develop.R;
import com.wkey.develop.ui.NetworkActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * <p>Title: FliperView</p>
 * <p>Description: FliperView</p>
 * <p>Copyright (c) 2012 www.oppo.com Inc. All rights reserved.</p>
 * <p>Company: OPPO</p>
 *
 * @author  Dante.Loong
 * @version 1.0
 *
 */

public class FlipperView extends ViewGroup {

	private Scroller mScroller = null;
	private View menu;
	private View main;
	private int i=0;
	private int width ;
	private int sidewidth;
	Context mContext;
	
	public FlipperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		width = getWidth();
		sidewidth= (int)(width * 0.75);
        distance = getWidth() * 3 / 4;
		/*
        menu = getChildAt(0);// 
        menu.setVisibility(VISIBLE);
        menu.measure(
            MeasureSpec.makeMeasureSpec((int) (getWidth() * 0.075), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        menu.layout(-(int) (getWidth() * 0.075), 0, 0, getHeight());
*/
        main = getChildAt(0);// 
        main.setVisibility(VISIBLE);
        //  
        main.measure(
            MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        main.layout(0, 0, getWidth(), getHeight());
	}
    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub  
        // 
    	//System.out.println("wkey:1");
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
            //System.out.println("wkey:2");
        }
        
    }
	public void slidingMenu() {
		// TODO Auto-generated method stub
		System.out.println("sliding:"+i);
		if(i==0){
			mScroller.startScroll(0, 0, (int) -(width*0.75), 0,1000);
			i=1;
		}else{
			mScroller.startScroll((int) -(width*0.75), 0, (int)(width*0.75), 0,1000);
			i=0;
		}
		invalidate();//重绘才会执行 computeScroll() 函数
	}
	
////////////////////////////////////////
	
	
    public void onSliding(float distanceX,float Vel) {
        //System.out.println("slidingxxxxx11x");
    	if(distanceX==1000){
    		System.out.println("distance:"+i);
    		if(i==1){	//菜单已开
    			if(Vel<-300 || Vel<300 && getScrollX()+sidewidth>sidewidth/3){	//大于1/3，关闭菜单
    				mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
    				i=0;
    			}else{
    				mScroller.startScroll(getScrollX(), 0, -(sidewidth+getScrollX()), 0);
    			}
    		}else{		//菜单已关
    			if(Vel>300 || Vel>-300 && -getScrollX()>sidewidth/3){		//大于1/3，打开菜单
    				mScroller.startScroll(getScrollX(), 0, -(sidewidth+getScrollX()), 0);
    				i=1;
    			}
    			else
    				mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
    			
    		}
    		
    	}
        if (getScrollX() + distanceX <= 0 && getScrollX() + distanceX >= -distance) {
            mScroller.startScroll(getScrollX(), 0, (int) distanceX, 0, 1);
            invalidate();
           // System.out.println("slidingxxxxxxxx="+getScrollX()+" \t"+distanceX);
        }
        invalidate();
        
    }
    
    private float mLastMotionX;
    private float mLastMotionY;
    private static final int TOUCH_STATE_REST = 0;
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private static final int TOUCH_STATE_SCROLLING = 1;
/////////////////////////////////////////
/*    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            //return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE :
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                final int yDiff = (int) Math.abs(mLastMotionY - y);
                System.out.println(xDiff);
                if (i==1) {
                    if (xDiff > mTouchSlop) {
                        mTouchState = TOUCH_STATE_SCROLLING;
                    }
                } else {
                    if (xDiff > mTouchSlop && xDiff > yDiff * 3) {
                       // mTouchState = TOUCH_STATE_SCROLLING;
                    }
                }
                //return false;
                break;

            case MotionEvent.ACTION_DOWN :
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                return false;//break;

            case MotionEvent.ACTION_CANCEL :
            case MotionEvent.ACTION_UP :
                mTouchState = TOUCH_STATE_REST;
                break;
        }
       // System.out.println(mTouchState +" "+mTouchSlop);
        return false;//mTouchState != TOUCH_STATE_REST;
    }*/
///////////////////////////////
    private VelocityTracker mVelocityTracker;
    private boolean touchStatus = false;
    private int distance;
//////////////////////////
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
    	//System.out.println(event.toString());
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        touchStatus = true;
        final float x = event.getX();
        final float y = event.getY();
        if (i==1 && x < distance && -distance == getScrollX()) {
        	System.out.println("weyksdjflajsdkfjkasdjfkasdjfksd");
            return false;
        }
       // System.out.println("weyksdjflajsdkfjkasdjfkasdjfksd");
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN :
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE :
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                mLastMotionY = y;
                if (true) {
                    onSliding(deltaX,0);
                }
               // mVelocityTracker.computeCurrentVelocity(1000);
                System.out.println(mVelocityTracker.getXVelocity());
                break;
            case MotionEvent.ACTION_UP :
                mTouchState = TOUCH_STATE_REST;
                mVelocityTracker.computeCurrentVelocity(1000);
              //  System.out.println(mVelocityTracker.getXVelocity());
    //            if (!AllNoteActivity.isSelectMode()) {
                  //  if (!menuVisible) {
                 //       showMenu();
                //    } else {
                //        hideMenu();
                //    }
        onSliding(1000,mVelocityTracker.getXVelocity());
     //           }
                break;
            case MotionEvent.ACTION_CANCEL :
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        System.out.println("wkey:"+action);
        return true;
    }

}


/*
public class FlipperView extends ViewGroup {

    private int distance;

    public int getDistance() {
        return distance;
    }

    private View menu;
    private View main;
    private Scroller scroller;
    private boolean menuVisible = false;
    private float mLastMotionX;
    private float mLastMotionY;
    private static final int TOUCH_STATE_REST = 0;
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private VelocityTracker mVelocityTracker;
    private boolean touchStatus = false;

    public boolean isMenuVisible() {
        return menuVisible;
    }

    public FlipperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub  
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub  

        distance = getWidth() * 3 / 4;

        // 
        menu = getChildAt(0);// 
        menu.setVisibility(VISIBLE);
        menu.measure(
            MeasureSpec.makeMeasureSpec((int) (getWidth() * 0.075), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        menu.layout(-(int) (getWidth() * 0.075), 0, 0, getHeight());

      //  main = getChildAt(1);// 
      //  main.setVisibility(VISIBLE);
        //  
     //   main.measure(
     //       MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
     //       MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
     //   main.layout(0, 0, getWidth(), getHeight());

    }

    public void showMenu() {
        // 没有超过半屏 
        if (getScrollX() > -getWidth() / 6) {
            scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
            menuVisible = false;
        }
        // 超过半屏 
        else {
            scroller.startScroll(getScrollX(), 0, -(distance + getScrollX()), 0, 500);
            menuVisible = true;
        }
        invalidate();
    }

    public void hideMenu() {
        if (getScrollX() <= -getWidth() * 2 / 3) {
            scroller.startScroll(getScrollX(), 0, -(distance + getScrollX()), 0, 300);
            menuVisible = true;
        } else {
            scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 500);
            menuVisible = false;
        }
        invalidate();
    }

    public void openMenu() {
        if (!menuVisible) {
            menuVisible = true;
            scroller.startScroll(getScrollX(), 0, -(distance + getScrollX()), 0, 500);
            invalidate();
        }
    }

    public void closeMenu() {
        if (menuVisible) {
            menuVisible = false;
            scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 500);
            invalidate();
        }
    }

    public void closeMenu_f() {
        if (menuVisible) {
            menuVisible = false;
            scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 0);
            invalidate();
        }
    }

    public void slidingMenu() {
        if (!menuVisible) {
            openMenu();
        } else {
            closeMenu();
        }
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub  
        // 
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public void onSliding(float distanceX) {
        if (getScrollX() + distanceX <= 0 && getScrollX() + distanceX >= -distance) {
            scroller.startScroll(getScrollX(), 0, (int) distanceX, 0, 1);
            invalidate();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE :
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                final int yDiff = (int) Math.abs(mLastMotionY - y);
                if (!menuVisible) {
                    if (xDiff > mTouchSlop && xDiff > yDiff * 3) {
                        mTouchState = TOUCH_STATE_SCROLLING;
                    }
                } else {
                    if (xDiff > mTouchSlop) {
                        mTouchState = TOUCH_STATE_SCROLLING;
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN :
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_CANCEL :
            case MotionEvent.ACTION_UP :
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        touchStatus = true;
        final float x = event.getX();
        final float y = event.getY();
        if (menuVisible && x < distance && -distance == getScrollX()) {
            return false;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN :
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE :
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                mLastMotionY = y;
              //  if (!SyncronizeService.isSyncronizing() && !AllNoteActivity.isSelectMode()) {
               //     onSliding(deltaX);
              //  }
                break;
            case MotionEvent.ACTION_UP :
                mTouchState = TOUCH_STATE_REST;
              //  if (!AllNoteActivity.isSelectMode()) {
              //      if (!menuVisible) {
               //         showMenu();
              //      } else {
             //           hideMenu();
              //      }
              //  }
                break;
            case MotionEvent.ACTION_CANCEL :
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return true;
    }
}
*/