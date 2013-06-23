package com.oppo.transfer.core.socket;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.oppo.transfer.core.utils.Constants;
import com.oppo.transfer.core.utils.TransInfo;
import com.oppo.transfer.core.utils.TransInfo_bak;



public class Client {
	Socket client = null;
	TransInfo_bak transInfo = null;
	FileSender sender = null;
	
	public Client(String state,String path,List<String> paths){
		//setCtrState(Constants.DIR,path,null);
		setCtrState(state,path,paths);
		client = SocketUtils.getInstance().getClientInstance(null);
		if(client!=null)
        try {
			sender = new FileSender(client);
			System.out.println("createclient ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		else return;
		send(client);
		//ReceiveFile(client);
	}
	
	public Client(TransInfo transInfo){
		setCtrState(transInfo);
		
		client = SocketUtils.getInstance().getClientInstance(transInfo.IP,transInfo.Port);
		if(client!=null)
        try {
			sender = new FileSender(client);
			System.out.println("createclient ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		else return;
		send(client);
		//ReceiveFile(client);
	}
	
	
	
	private void send(Socket socket){
		try {
			String Req = requesttosend();
			if(Req.equals(Constants.ALLOW)){
				
			}else if(Req.equals(Constants.DENY)){
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		sendInfo(socket);	//确定同步模式还是异步模式
		if(false){	//同步模式
			
		}else{
			sendContent(socket);
			if(transInfo == null){
				//sendFile(socket,"");
			}else{
				
			}
			sendComplete(socket); //结束控制符
		}
	}
	
	private String requesttosend() throws IOException {
		// TODO Auto-generated method stub
		sender.sendMsg(Constants.REQSEND);
		//等待接收的返回状态
		return sender.getMsg();
	}

	String path = null;
	List<String> paths = null;
	
	public void setCtrState(TransInfo transInfo){
		if(transInfo.Path!=null){
			File file = new File(transInfo.Path);
			if(file.isFile()){
				setCtrState(Constants.FILE,transInfo.Path,null);
			}else if(file.isDirectory()){
				setCtrState(Constants.DIR,transInfo.Path,null);
			}
		}else if(transInfo.Paths!=null){
			setCtrState(Constants.DEFINED,null,transInfo.Paths);
		}
	}
	
	public void setCtrState(String state,String path,List<String> paths){
		Constants.CTR_STATE = state;
		this.path = path;
		if(Constants.CTR_STATE.equals(Constants.DEFINED)){
			this.paths = paths;
		}
	}


	
	private void sendInfo(Socket socket){
		
	}
	
	private void sendComplete(Socket socket){
		sender.sendMsg(Constants.END);
	}
	
	private void sendContent(Socket socket){
		//fileToTransfer

		sender.sendMsg(Constants.CTR_STATE);

		if(Constants.CTR_STATE.equals(Constants.FILE)){	//file 为文件
			//发送文件信息，包括 文件名，文件大小，filename,size
			sendFile(socket,path);
			return;
			
		}else if(Constants.CTR_STATE.equals(Constants.DIR)){		//file 为文件夹
			//扫描，保持文件夹信息
			transInfo = null;
			//发送文件夹信息，文件夹结构，文件数，文件名数组，文件大小数组，
			//folderStruct, fileNumber,filename array, file size array
			int i = path.lastIndexOf("/");
			String src = path.substring(0,i+1);
			System.out.println(src);
			sendFolder(socket,path,src);
			
		}else if(Constants.CTR_STATE.equals(Constants.DEFINED)){
			sendDefined(paths);
		}
		
		
	}
	
	private void sendFile(Socket socket,String path){
		//控制字段
		if(Constants.CTR_STATE.equals(Constants.FILE)){
			;
		}else{
			sender.sendMsg(Constants.FILE);
		}
		//
		System.out.println("path");
		//
        try {
            sender.sendFile(path);
        } catch (Exception ex) {
            System.out.println("Error communicating with client: " + ex.getMessage());
        }
	}
	
	////////////////////////////////////////////////////////
	private void sendFile(Socket socket,String path,String src,String filename){
		//控制字段
		if(Constants.CTR_STATE.equals(Constants.FILE)){
			sender.sendMsg(Constants.ONLYFILE);
		}else{
			sender.sendMsg(Constants.FILE);
			sender.sendMsg(getPath(path, src, filename));
		}
		
		//
		//System.out.println("path");
		//
        try {
            sender.sendFile(path);
        } catch (Exception ex) {
            System.out.println("Error communicating with client: " + ex.getMessage());
        }
	}
	
    public String getPath(String all, String pre,String filename)// 已知绝对路径与根目录、文件名，获得文件相对路径
    {// all为绝对路径，pre为根
        int l1 = pre.length();// 根路径长度
        int l2=filename.length();//文件名长度
        int l=all.length();
        String r = all.substring(l1,l-l2-1);
        return r;
    }

	 public String getPath(String all, String pre)// 已知绝对路径与根目录，获得相对路径
	 {// all为绝对路径，pre为根
	 	int l = pre.length();// 根路径长度
	 	String r = all.substring(l);
	 	System.out.println(pre);
	 	System.out.println(all);
	 	return r;
	 }
	////////////////////////////////////////////
	private void sendDir(Socket socket,String path,String src){
		/**发送文件夹信息*/
		sender.sendMsg(Constants.DIR);
		sender.sendMsg(getPath(path,src));	//相对路径

		/**发送文件夹文件流*/
		//sendFile(socket,path);
	}
	
	private void sendFolder(Socket socket,String path,String src){
		File file = new File(path);
		if(!file.exists() || !file.isDirectory()){
			return;
		}
		//发送文件夹控制字段
		sendDir(socket,path,src);
		//
		File[] temp = file.listFiles();
		for(int i = 0 ; i<temp.length;i++){
			if(temp[i].isFile()){
				sendFile(socket,temp[i].getAbsolutePath(),src,temp[i].getName());
			}else if(temp[i].isDirectory()){
				sendFolder(socket,temp[i].getAbsolutePath(),src);
			}
		}
		
	}
	
	private void sendDefined(List<String> path){
		
		List<String> paths = path;
		for(String pa :paths){
			File file = new File(pa);
			if(file.isFile()){
				sendFile(null,pa);
			}else if(file.isDirectory()){
				sendFolder(null,"","");
			}
		}
	}
	


}
