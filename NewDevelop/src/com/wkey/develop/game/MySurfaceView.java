package com.wkey.develop.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder holder;
	Paint mPaint;
	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = this.getHolder();//获取holder  
        holder.addCallback(this);  
        mPaint = new Paint();  
        mPaint.setColor(Color.BLUE);  
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		new Thread(new MyThread()).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	Canvas canvas;
    //内部类的内部类  
    class MyThread implements Runnable{  

        @Override  
        public void run() {  
        	if(false){
            canvas = holder.lockCanvas(null);//获取画布  

            //int density=2;
            //canvas.drawRect(new RectF(40*density,60*density,300*density,300*density), mPaint); 
            paint(canvas);

            holder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像  
        	}else
            while(true) 
            {
            	canvas = holder.lockCanvas(null);//获取画布  
            	paintLeaf(canvas);
            	holder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像  
            }
        }  
          
    }  
    
    
    		public void paint(Canvas g) {
    	         //g.drawString("静听松声寒!!", 35, 50 );
    	         tree(700.0,1600.0,600.0,0.0,9,g);
    	     }
    	     public void tree(double x1,double y1,double x2,double y2,int n,Canvas g)
    	     {
    	         if(n>=1){  
    	             double x3,x4,y3,y4;
    	       //       g.drawString("I believe !",0,300);
    	             
    	            // new Thread(new Runnable(){
					//	@Override
					//	public void run() {
							// TODO Auto-generated method stub
    	             		mPaint.setARGB(255, (int)x1/3%255, (int)y1/3%255, (int)x2/3%255);
		    	             g.drawLine((int)(x1),(int)(y1),(int)(x2),(int)(y2),mPaint);  
					//	}
    	            // }).start();
    	             
    	             x3=(x1+x2)/2;
    	             y3=(y1+y2)/2;
    	             //tree(x1,y1,x3,y3,n-1,g);//
    	             tree(x3,y3,x2,y2,n-1,g);//
      
    	             x4=(x2+y2-y3-x3)*0.7071+x3;
    	             y4=(y2-x2+x3-y3)*0.7071+y3;
    	             tree(x3,y3,x4,y4,n-1,g);   //        
    	             x3=(x1*3+x2)/4;
    	             y3=(y1*3+y2)/4;
    	             
    	             x2=(x2*3+x1)/4;
    	             y2=(y2*3+y1)/4;
    	             x4=(x2*1.732-y2+2*x3-x3*1.732+y3)/2;
    	             y4=(x2+y2*1.732-x3+2*y3-1.732*y3)/2;
    	             tree(x3,y3,x4,y4,n-1,g);//
    	         }
    	     }
    	     
    	     boolean dstatus = true;
    	     float D=-25;//树的弯曲角度C
    	     float K=40;//树杈的伸展角度B
    	     
    	     
    	     
    	     void paintLeaf(Canvas g){
    	         if (dstatus) 
    	         {
    	             D += 0.2f;
    	             if (D>=25) dstatus = false;
    	         }
    	         else 
    	         {
    	             D -= 0.2f;
    	             if (D<=-25) dstatus = true;
    	         }
    	         
    	         if (K<60) K=K+0.2f;
    	        // Canvas offscreenbuffer;
    	         //清除缓冲内的图形
    	        // offscreenbuffer.clearRect(0,0,600,600);
    	         mPaint.setColor(Color.BLACK);
    	        g.drawRect(0, 0, 1200, 2400, mPaint);
    	         //设置颜色
    	        // offscreenbuffer.setColor(Color.black); 
    	          //在offscreenbuffer中画树
    	         //mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    	        mPaint.setColor(Color.BLUE);
    	         drawLeaf(g,400, 1200, 90,270,K,D);
    	         //mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
    	         //将缓冲内的图形放到屏幕
    	        // g.drawImage(offscreenimage, 0, 0, this);
    	     }
    	     
    	     public static final double PI = Math.PI / 180;
    	     public void drawLeaf(Canvas g,double x,double y,
    	                    double L, double a,float B,float C)
    	     {    
    	         double x1,x2,x1L,x2L,x2R,x1R,
    	                y1,y2,y1L,y2L,y2R,y1R;
    	        
    	         float s1 = 8;
    	         float s2 = 3;
    	         float s3 = 1.1f;

    	         if(L > s1)
    	         {
    	             x2 = x + L * Math.cos(a * PI);
    	             y2 = y + L * Math.sin(a * PI);
    	             x2R = x2 + L / s2 * Math.cos((a + B) * PI);
    	             y2R = y2 + L / s2 * Math.sin((a + B) * PI);
    	             x2L = x2 + L / s2 * Math.cos((a - B) * PI);
    	             y2L = y2 + L / s2 * Math.sin((a - B) * PI);
    	   
    	             x1 = x + L / s2 * Math.cos(a * PI);
    	             y1 = y + L / s2 * Math.sin(a * PI);
    	             x1L = x1 + L / s2 * Math.cos((a - B) * PI);
    	             y1L = y1 + L / s2 * Math.sin((a - B) * PI);
    	             x1R = x1 + L / s2 * Math.cos((a + B) * PI);
    	             y1R = y1 + L / s2 * Math.sin((a + B) * PI);
    	              
    	             g.drawLine((int) x, (int) y, (int) x2, (int) y2,mPaint);
    	             g.drawLine((int) x2, (int) y2, (int) x2R, (int) y2R,mPaint);
    	             g.drawLine((int) x2, (int) y2, (int) x2L, (int) y2L,mPaint);
    	             g.drawLine((int) x1, (int) y1, (int) x1L, (int) y1L,mPaint); 
    	             g.drawLine((int) x1, (int) y1, (int) x1R, (int) y1R,mPaint);
    	           
    	             drawLeaf(g, x2, y2, L / s3, a + C,B,C);
    	             drawLeaf(g, x2R, y2R, L / s2, a + B,B,C);
    	             drawLeaf(g, x2L, y2L, L / s2, a - B,B,C);
    	             drawLeaf(g, x1L, y1L, L / s2, a - B,B,C);
    	             drawLeaf(g, x1R, y1R, L / s2, a + B,B,C);   
    	         }
    	     }

}
