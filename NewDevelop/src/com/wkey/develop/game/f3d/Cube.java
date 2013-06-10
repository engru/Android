package com.wkey.develop.game.f3d;

import javax.microedition.khronos.opengles.GL10;
import static com.wkey.develop.game.f3d.Constant.*;


public class Cube {
	//���ڻ��Ƹ��������ɫ����

	
	public void drawSelf(GL10 gl,float UNIT_SIZE)
	{
		//�ܻ���˼�룺ͨ����һ����ɫ������ת��λ��������ÿ�����λ��
		//�����������ÿ����
		ColorRect cr=new ColorRect(UNIT_SIZE/2,UNIT_SIZE/2);
		
		gl.glPushMatrix();
		
		//����ǰС��
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, UNIT_SIZE*SCALE);
		cr.drawSelf(gl);		
		gl.glPopMatrix();
		
		//���ƺ�С��
		gl.glPushMatrix();		
		gl.glTranslatef(0, 0, -UNIT_SIZE*SCALE);
		gl.glRotatef(180, 0, UNIT_SIZE, 0);
		cr.drawSelf(gl);		
		gl.glPopMatrix();
		
		//�����ϴ���
		gl.glPushMatrix();			
		gl.glTranslatef(0,UNIT_SIZE*SCALE,0);
		gl.glRotatef(-90, UNIT_SIZE, 0, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//�����´���
		gl.glPushMatrix();			
		gl.glTranslatef(0,-UNIT_SIZE*SCALE,0);
		gl.glRotatef(90, UNIT_SIZE, 0, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//���������
		gl.glPushMatrix();			
		gl.glTranslatef(UNIT_SIZE*SCALE,0,0);		
		gl.glRotatef(-90, UNIT_SIZE, 0, 0);
		gl.glRotatef(90, 0, UNIT_SIZE, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//�����Ҵ���
		gl.glPushMatrix();			
		gl.glTranslatef(-UNIT_SIZE*SCALE,0,0);		
		gl.glRotatef(90, UNIT_SIZE, 0, 0);
		gl.glRotatef(-90, 0, UNIT_SIZE, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		gl.glPopMatrix();
	}
	

}
