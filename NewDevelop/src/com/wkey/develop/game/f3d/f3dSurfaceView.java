package com.wkey.develop.game.f3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

public class f3dSurfaceView extends GLSurfaceView{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��	

    private float mPreviousX;//�ϴεĴ���λ��X����
	
	public f3dSurfaceView(Context context) {
        super(context);
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            mRenderer.angle += dx * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            
            requestRender();//�ػ滭��
        }   
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {   
    	Cube cube=new Cube();//������
    	float angle=45;//����ת�Ƕ�
    	int i=1;
    	//GL10 gl;
        public void onDrawFrame(GL10 gl) {
        	//while(true)
        		onDrawFrame1(gl);
        }
    	
        public void onDrawFrame1(GL10 gl) {
        	System.out.println("drawframe");
        	//gl = mgl;
    		//����Ϊ�򿪱������
    		gl.glEnable(GL10.GL_CULL_FACE);

    		//������ɫģ��Ϊƽ����ɫ   
            gl.glShadeModel(GL10.GL_SMOOTH);
        	
        	//�����ɫ��������Ȼ���
        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        	//���õ�ǰ����Ϊģʽ����
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            //���õ�ǰ����Ϊ��λ����
            gl.glLoadIdentity();    
            
            //����cameraλ��=======================
            
            GLU.gluLookAt//��̫���ܱ��ε��ӽǡ���С�ӽ�
            (
            		gl, 
            		0f,   //����λ�õ�X
            		40f, 	//����λ�õ�Y
            		60.0f,   //����λ�õ�Z
            		0, 	//�����򿴵ĵ�X
            		0f,   //�����򿴵ĵ�Y
            		0,   //�����򿴵ĵ�Z
            		0, 
            		1, 
            		0
            );  
            
            //��ת������ϵ
            gl.glRotatef(angle, 0, 1, 0);
            /*
            //������������
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, -2);
            cube.drawSelf(gl);
            gl.glPopMatrix();
            
            //������������
            gl.glPushMatrix();
            gl.glTranslatef(2, 0, 0);
            cube.drawSelf(gl);
            gl.glPopMatrix();
            */
          //  if(i>deep)
          //  	i=deep;
          //  else
          //      i++;    
            i=i%deep;
            if(i==0){
            	draw(sx,sy,sz,deep,gl,6);
			}
            else draw(sx,sy,sz,i,gl,6);

            i++;
            try {
				new Thread().sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(i);
            
        }
        static final int deep = 8;
        static final int sx=0,sy=0,sz=-2;
        public void draw(float x,float y,float z,int n,GL10 gl,float size){
        	if(n>=1){
        		float x2 ,x3,y2,y3,z2,z3,size2;
	        //������������


		        if(n==1)//||n==2)
		        	//cube.drawSelf(gl,size);
		        	//new Thread(new MyThread(gl)).start();
		        	new MyThread(gl,x,y,z,size).run();

		        
		        size2 = size/2;

		        if(z+size>sz)
		        	;//draw(x,y,z+size,n-1,gl,size2);
		        if(z-size<sz)
		        	draw(x,y,z-size,n-1,gl,size2);
		        if(y+size>sy)
		        	draw(x,y+size,z,n-1,gl,size2);
		        if(y-size<sz)
		        	;//draw(x,y-size,z,n-1,gl,size2);
		        if(x+size>sx)
		        	draw(x+size,y,z,n-1,gl,size2);
		        if(x-size<sx)
		        	;//draw(x-size,y,z,n-1,gl,size2);
        	}
        }
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	gl.glViewport(0, 0, width, height);
        	//���õ�ǰ����ΪͶӰ����
            gl.glMatrixMode(GL10.GL_PROJECTION);
            //���õ�ǰ����Ϊ��λ����
            gl.glLoadIdentity();
            //����͸��ͶӰ�ı���
            float ratio = (float) height/width ;
            //���ô˷����������͸��ͶӰ����
            //gl.glFrustumf( -1, 1,-ratio, ratio, 1, 100);   //���ܱ��ε��ӽǡ������ӽ�  
            gl.glFrustumf( -1, 1,-ratio, ratio, 8f, 100);     //��̫���ܱ��ε��ӽǡ���С�ӽ�
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //�رտ����� 
        	gl.glDisable(GL10.GL_DITHER);
        	//�����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
            //������Ļ����ɫ��ɫRGBA
            gl.glClearColor(0,0,0,0);            
            //������Ȳ���
            gl.glEnable(GL10.GL_DEPTH_TEST);
        }
        

        class MyThread implements Runnable{  
        	float x,y,z,size;
        	private GL10 gl;
        	public MyThread(GL10 gl,float x1,float y1,float z1,float size1){
        		this.gl = gl;
        		x=x1;y=y1;z=z1;
        		size =size1;
        	}
            @Override  
            public void run() {
		        gl.glPushMatrix();
		        gl.glTranslatef(x, y, z);
            	cube.drawSelf(gl,size);
		        gl.glPopMatrix();
            }  
              
        }  
 
    }

}
