package com.wkey.develop.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

public class DragListView extends DynamicListView implements OnGestureListener {
	public DragListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	//public DragListView(Context context, AttributeSet attrs) {
	//	super(context, attrs);
		// TODO Auto-generated constructor stub
	//}

	//over ride the super
	protected void init(Context context) {
		super.init(context);
	}
	/*
	public boolean onInterceptTouchEvent(MotionEvent event) {

		return false; 
		
	}
	

	
	public boolean onTouchEvent(MotionEvent event) {
		boolean bl = super.onTouchEvent(event);
		final int action = event.getAction(); 
		switch (action) {
			case MotionEvent.ACTION_DOWN:  
		        // ���µ�ʱ����ʾ������ͼ  
				onCreateFloatView(pointToPosition((int)event.getX(), (int)event.getY()));  
		        break;  
		  
		    case MotionEvent.ACTION_UP:  
		    case MotionEvent.ACTION_CANCEL:  
		        // ̧��ʱ���������ͼ  
		        //destroyFloatView();  
		        break;
		    case MotionEvent.ACTION_MOVE:
		    	mImageView.
		    	break;
		}
		return bl;
		
	}
	
	
	Bitmap mFloatBitmap = null;
	ImageView mImageView = null;
    public View onCreateFloatView(int position) {
        // Guaranteed that this will not be null? I think so. Nope, got
        // a NullPointerException once...
        View v = this.getChildAt(position-1 + this.getHeaderViewsCount() - this.getFirstVisiblePosition());

        if (v == null) {
            return null;
        }

        v.setPressed(false);

        // Create a copy of the drawing cache so that it does not get
        // recycled by the framework when the list tries to clean up memory
        //v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        v.setDrawingCacheEnabled(true);
        mFloatBitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        if (mImageView == null) {
            mImageView = new ImageView(this.getContext());
        }
        mImageView.setBackgroundColor(7377883);
        mImageView.setPadding(0, 0, 0, 0);
        mImageView.setImageBitmap(mFloatBitmap);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(v.getWidth(), v.getHeight()));
        
        measureFloatView();  
        
        // ���ص�ǰItem  
        v.setVisibility(View.INVISIBLE);  
      
        // ʹ����������Ч  
       // requestLayout(); 
        return mImageView;
    }
	
	private void measureFloatView() {
		if (mImageView != null) {
			measureItem(mImageView);
		}
	}
	
	
	
	
	
	
	
    private void measureItem(View item) {
    	// ����LayoutParams
        ViewGroup.LayoutParams lp = item.getLayoutParams();
        if (lp == null) {
            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            item.setLayoutParams(lp);
        }
        
        // ������Ȳ������
        int wspec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, getListPaddingLeft()
                + getListPaddingRight(), lp.width);
        
        // �����߶Ȳ������
        int hspec;
        if (lp.height > 0) {
            hspec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        } else {
            hspec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        
        // ���¶Ե�ǰItem���в�������
        item.measure(wspec, hspec);
    }
	
    
    boolean mFloatViewOnMeasured;
    int mWidthMeasureSpec;
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (mImageView != null) {
			if (mImageView.isLayoutRequested()) {
				measureFloatView();
			}
			mFloatViewOnMeasured = true;
		}
		
		mWidthMeasureSpec = widthMeasureSpec;
	}
	
	
	protected void layoutChildren() {
		super.layoutChildren();
		
		if (mImageView != null) {
			if (mImageView.isLayoutRequested() && !mFloatViewOnMeasured) {
				measureFloatView();
			}
			
			// ������ߺ�ָ��λ�� 
			mImageView.layout(0, -200, mImageView.getMeasuredWidth(), mImageView.getMeasuredHeight());
			mFloatViewOnMeasured = false;
		}
		
	}
	
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		
		if (mImageView != null) {
			int width = mImageView.getWidth();
			int height = mImageView.getHeight();
			
			canvas.save();
			// ���Ƶ���ǰListView���Ͻ�Ϊ����ԭ�����Ӧλ��
			canvas.translate(0, 30);
			canvas.clipRect(0, 0, width, height);
			
			// ������ͼ���Ƶ��ص��ṩ�ĵ�ǰCanvas��
			mImageView.draw(canvas);
			canvas.restore();
		}
	}
	
	*/
	
	
	/////////////////////////////////////////////

    private ImageView dragImageView;//����ק�����ʵ����һ��ImageView
    private int dragSrcPosition;//��ָ�϶���ԭʼ���б��е�λ��
    private int dragPosition;//��ָ�϶���ʱ�򣬵�ǰ�϶������б��е�λ��
    
    private int dragPoint;//�ڵ�ǰ�������е�λ��
    private int dragOffset;//��ǰ��ͼ����Ļ�ľ���(����ֻʹ����y������)
    
    private WindowManager windowManager;//windows���ڿ�����
    private WindowManager.LayoutParams windowParams;//���ڿ�����ק�����ʾ�Ĳ���
    
    private int scaledTouchSlop;//�жϻ�����һ������
    private int upScrollBounce;//�϶���ʱ�򣬿�ʼ���Ϲ����ı߽�
    private int downScrollBounce;//�϶���ʱ�򣬿�ʼ���¹����ı߽�
    GestureDetector mDetector;
    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(getContext(), this);
        
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    
    //����touch�¼�����ʵ���Ǽ�һ�����
    //@Override
   // public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*if(ev.getAction()==MotionEvent.ACTION_DOWN){
            int x = (int)ev.getX();
            int y = (int)ev.getY();
            
            dragSrcPosition = dragPosition = pointToPosition(x, y);
            if(dragPosition==AdapterView.INVALID_POSITION){
                return super.onInterceptTouchEvent(ev);
            }

            ViewGroup itemView = (ViewGroup) getChildAt(dragPosition-getFirstVisiblePosition());
            dragPoint = y - itemView.getTop();
            dragOffset = (int) (ev.getRawY() - y);
            
            View dragger = itemView;//.findViewById(R.id.drag_list_item_image);
            if(dragger!=null&&x>dragger.getLeft()-20){
                //
                upScrollBounce = Math.min(y-scaledTouchSlop, getHeight()/3);
                downScrollBounce = Math.max(y+scaledTouchSlop, getHeight()*2/3);
                
                itemView.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
                startDrag(bm, y);
            }
            return false;
         }*/
    //     return super.onInterceptTouchEvent(ev);
    //}

    /**
     * �����¼�
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	mDetector.onTouchEvent(ev);
        if(dragImageView!=null&&dragPosition!=INVALID_POSITION){
            int action = ev.getAction();
            switch(action){
                case MotionEvent.ACTION_UP:
                    int upY = (int)ev.getY();
                    stopDrag();
                    onDrop(upY);
                    ViewGroup itemView = (ViewGroup) getChildAt(dragSrcPosition-getFirstVisiblePosition());
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveY = (int)ev.getY();
                    onDrag(moveY);

                    break;
                default:break;
            }
            return true;
        }
        //Ҳ������ѡ�е�Ч��
        return super.onTouchEvent(ev);
    }
    
    /**
     * ׼���϶�����ʼ���϶����ͼ��
     * @param bm
     * @param y
     */
    public void startDrag(Bitmap bm ,int y){
        stopDrag();
        
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - dragPoint + dragOffset;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager)getContext().getSystemService("window");
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }
    
    /**
     * ֹͣ�϶���ȥ���϶����ͷ��
     */
    public void stopDrag(){
        if(dragImageView!=null){
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }
    
    /**
     * �϶�ִ�У���Move������ִ��
     * @param y
     */
    public void onDrag(int y){
        if(dragImageView!=null){
            windowParams.alpha = 0.8f;
            windowParams.y = y - dragPoint + dragOffset;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
        //Ϊ�˱��⻬�����ָ��ߵ�ʱ�򣬷���-1������
        int tempPosition = pointToPosition(0, y);
        if(tempPosition!=INVALID_POSITION){
            dragPosition = tempPosition;
        }
        
        //����
        int scrollHeight = 0;
        if(y<upScrollBounce){
            scrollHeight = 8;//�������Ϲ���8�����أ�����������Ϲ����Ļ�
        }else if(y>downScrollBounce){
            scrollHeight = -8;//�������¹���8�����أ�������������Ϲ����Ļ�
        }
        
        if(scrollHeight!=0){
            //���������ķ���setSelectionFromTop()
            setSelectionFromTop(dragPosition, getChildAt(dragPosition-getFirstVisiblePosition()).getTop()+scrollHeight);
        }
    }
    
    /**
     * �϶����µ�ʱ��
     * @param y
     */
    public void onDrop(int y){
        
        //Ϊ�˱��⻬�����ָ��ߵ�ʱ�򣬷���-1������
        int tempPosition = pointToPosition(0, y);
        if(tempPosition!=INVALID_POSITION){
            dragPosition = tempPosition;
        }
        
        //�����߽紦��
        if(y<getChildAt(1).getTop()){
            //�����ϱ߽�
            dragPosition = 1;
        }else if(y>getChildAt(getChildCount()-1).getBottom()){
            //�����±߽�
            dragPosition = getAdapter().getCount()-1;
        }
        
        //���ݽ���
        if(dragPosition>0&&dragPosition<getAdapter().getCount()){
            //@SuppressWarnings("unchecked")
            //DragListAdapter adapter = (DragListAdapter)getAdapter();
            //String dragItem = adapter.getItem(dragSrcPosition);
            //adapter.remove(dragItem);
            //adapter.insert(dragItem, dragPosition);
            //Toast.makeText(getContext(), adapter.getList().toString(), Toast.LENGTH_SHORT).show();
        }
        
    }

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("ondown===================");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent ev) {
		System.out.println("onlongpress===================");
		// TODO Auto-generated method stub
            int x = (int)ev.getX();
            int y = (int)ev.getY();
            
            dragSrcPosition = dragPosition = pointToPosition(x, y);
            if(dragPosition==AdapterView.INVALID_POSITION){
                return;//return super.onInterceptTouchEvent(ev);
            }

            ViewGroup itemView = (ViewGroup) getChildAt(dragPosition-getFirstVisiblePosition());
            dragPoint = y - itemView.getTop();
            dragOffset = (int) (ev.getRawY() - y);
            
            View dragger = itemView;//.findViewById(R.id.drag_list_item_image);
            if(dragger!=null&&x>dragger.getLeft()-20){
                //
                upScrollBounce = Math.min(y-scaledTouchSlop, getHeight()/3);
                downScrollBounce = Math.max(y+scaledTouchSlop, getHeight()*2/3);
                
                itemView.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
                startDrag(bm, y);
                itemView.setVisibility(View.INVISIBLE);
            }
         
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("onshowpress===================");
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
