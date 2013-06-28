package com.oppo.transfer.core.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class TransStateMachine{
	//外部设定刷新，调用更改状态，状态改变后，执行界面进度刷新操作。
	//如果后续增加状态操作，也可在此完成
	//Init ,连接
	//Negotiate ,协商
	//Transfer ,传输
	//Complete ,完成
	public static final int Init 		= 0;
	public static final int Negotiate	= 1;
	public static final int Transfer	= 2;
	public static final int Complete	= 3;
	
	private List<StateListener> sl = new ArrayList<StateListener>();
	private int CURRENT_STATE = Init;
	private int PROGRESS = 0;
	
	public TransStateMachine(){
		
	}
	
	
	public void registerSateListener(StateListener sl){
		this.sl.add(sl);
		//this.sl = sl;
	}
	
	public void registerSateListener(List<StateListener> sl){
		this.sl.addAll(sl);
		//this.sl = sl;
	}
	
	
	public void setState(int state){
		CURRENT_STATE = state;
		update();
	}
	
	public void setState(int state,int progress){
		//System.out.println("state:"+state+"|"+progress);
		CURRENT_STATE = state;
		PROGRESS = progress;
		update();
	}
	
	void update(){
		if(sl!=null)
			for(StateListener sl : this.sl){
				sl.updateState(CURRENT_STATE , PROGRESS);
			}
	}
	
	
}
