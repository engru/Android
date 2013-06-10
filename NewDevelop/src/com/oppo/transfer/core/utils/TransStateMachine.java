package com.oppo.transfer.core.utils;

import java.util.List;

public class TransStateMachine{
	//�ⲿ�趨ˢ�£����ø���״̬��״̬�ı��ִ�н������ˢ�²�����
	//�����������״̬������Ҳ���ڴ����
	//Init ,����
	//Negotiate ,Э��
	//Transfer ,����
	//Complete ,���
	public static final int Init 		= 0;
	public static final int Negotiate	= 1;
	public static final int Transfer	= 2;
	public static final int Complete	= 3;
	
	private List<StateListener> sl;
	private int state;
	private int progress;
	
	public TransStateMachine(){
		
	}
	
	
	void registerSateListener(StateListener sl){
		this.sl.add(sl);
		//this.sl = sl;
	}

	
	
	void setState(){
		update();
		
	}
	
	void update(){
		for(StateListener sl : this.sl){
			sl.updateState(state , progress);
		}
	}
	
	
}
