package com.oppo.transfer.core.socket;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.oppo.transfer.core.utils.Constants;
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
	
	private void send(Socket socket){
		sendInfo(socket);	//ȷ��ͬ��ģʽ�����첽ģʽ
		if(false){	//ͬ��ģʽ
			
		}else{
			sendContent(socket);
			if(transInfo == null){
				//sendFile(socket,"");
			}else{
				
			}
			sendComplete(socket); //�������Ʒ�
		}
	}
	
	String path = null;
	List<String> paths = null;
	
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

		if(Constants.CTR_STATE.equals(Constants.FILE)){	//file Ϊ�ļ�
			//�����ļ���Ϣ������ �ļ������ļ���С��filename,size
			sendFile(socket,path);
			return;
			
		}else if(Constants.CTR_STATE.equals(Constants.DIR)){		//file Ϊ�ļ���
			//ɨ�裬�����ļ�����Ϣ
			transInfo = null;
			//�����ļ�����Ϣ���ļ��нṹ���ļ������ļ������飬�ļ���С���飬
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
		//�����ֶ�
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
		//�����ֶ�
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
	
    public String getPath(String all, String pre,String filename)// ��֪����·�����Ŀ¼���ļ���������ļ����·��
    {// allΪ����·����preΪ��
        int l1 = pre.length();// ��·������
        int l2=filename.length();//�ļ�������
        int l=all.length();
        String r = all.substring(l1,l-l2-1);
        return r;
    }

	 public String getPath(String all, String pre)// ��֪����·�����Ŀ¼��������·��
	 {// allΪ����·����preΪ��
	 	int l = pre.length();// ��·������
	 	String r = all.substring(l);
	 	System.out.println(pre);
	 	System.out.println(all);
	 	return r;
	 }
	////////////////////////////////////////////
	private void sendDir(Socket socket,String path,String src){
		/**�����ļ�����Ϣ*/
		sender.sendMsg(Constants.DIR);
		sender.sendMsg(getPath(path,src));	//���·��

		/**�����ļ����ļ���*/
		//sendFile(socket,path);
	}
	
	private void sendFolder(Socket socket,String path,String src){
		File file = new File(path);
		if(!file.exists() || !file.isDirectory()){
			return;
		}
		//�����ļ��п����ֶ�
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
