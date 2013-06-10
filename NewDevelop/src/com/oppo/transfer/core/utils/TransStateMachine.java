package com.oppo.transfer.core.utils;

import java.util.List;

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
