package com.oppo.transfer.core.utils;

import java.util.List;

public class TransInfo {
	//传输信息又几个方面
	/**
	 * 1、 选择的文件，（包括文件、文件夹、自定义文件）（路径和路径列表）
	 * 2、 网络，对方的网络IP
	 * 3、 端口号，端口号本来可以使用默认，但内网情况下，需要使用协商
	 * 
	 */
	//Note:Path 和 Paths 属于互斥关系，即 Path 和 Paths 必有一个为 null
	public String 		Path 	;//= null;
	public List<String>	Paths 	;//= null;
	public String 		IP 		;//= Constants.P2P_IP;
	public int 			Port 	;//= Constants.DEFAULT_PORT;
	
	public void Init(){
		this.Path = null;
		this.Paths = null;
		this.IP = Constants.P2P_IP;
		this.Port = Constants.DEFAULT_PORT;
	}
	
	public TransInfo(){
		Init();
	}
	
	public TransInfo(String Path){
		this();
		this.Path = Path;
	}
	
	public TransInfo(String Path,String IP){
		this(Path);
		this.IP = IP;
	}
	public TransInfo(String Path,String IP,int Port){
		this(Path,IP);
		this.Port = Port;
	}
	
	public TransInfo(List<String> Paths){
		this();
		this.Paths = Paths;
	}
	
	public TransInfo(List<String> Paths,String IP){
		this(Paths);
		this.IP = IP;
	}
	
	public TransInfo(List<String> Paths,String IP,int Port){
		this(Paths,IP);
		this.Port = Port;
	}
	//////////////////////////////////////////////
	public void setPath(String Path){
		this.Path = Path;
	}
	
	public void setPaths(List<String> Paths){
		this.Paths = Paths;
	}
	
	public void setIP(String IP){
		this.IP = IP;
	}
	
	public void setPort(int Port){
		this.Port = Port;
	}
	
	public String getPath(){
		if(this.Path!=null){
			return this.Path;
		}
		return null;
	}
	
	public List<String> getPaths(){
		if(this.Paths!=null){
			return this.Paths;
		}
		return null;
	}
	
	public String getIP(){
		return this.IP;
	}
	
	public int getPort(){
		return this.Port;
	}
	
	

}
