package com.oppo.transfer.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TransInfo_bak {
	
	ArrayList<String> xx;
	List<File> ss;
	
	
	void sendDefindedInfo(Socket socket){
		
	}
	
	void sendFileInfo(Socket socket,File file,boolean bl) throws IOException{
		FileInfo fi = null ;
		fi.name = file.getName();
		fi.path = file.getCanonicalPath();
		fi.size = file.length();
		
		if(bl){
			//发送
		}else{
			return;
		}
			
	}
	
	void sendFolderInfo(Socket socket,File file) throws IOException{
		if(!file.exists() || !file.isDirectory()){
			return;
		}
		FolderInfos folderInfos = null;
		File[] temp = file.listFiles();
		for(int i=0; i<temp.length;i++){
			sendFileInfo(socket,temp[i],false);
			if(temp[i].isFile()){
				folderInfos.file.add(new FileInfo(temp[i]));
			}else if(temp[i].isDirectory()){
				folderInfos.folder.add(null);
				sendFolderInfo(socket,temp[i]);
			}
		}
		
		//发送
		
		
		
	}
	
	/***---------------------------------------------------**/
	
	interface Info{
		void saveInfo();
		void getInfo();
	}
	/***---------------------------------------------------**/
	
	class definedInfo{
		List<FolderInfo> folder;
		List<FileInfo> file;
	}
	
	class FolderInfos{
		FolderInfo folderInfo;		//目录信息
		List<FolderInfos> folder;	//子目录
		List<FileInfo> file;		//目录文件
		
		public FolderInfos(File file){
			
		}
	}
	class FolderInfo{
		FileInfo fileInfo;
	}
	class FileInfo implements Info{
		String name;
		String path;
		long size;

		public FileInfo(File file){
			name = file.getName();
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			size = file.length();
		}
		@Override
		public void saveInfo() {
			// TODO Auto-generated method stub
			
		}

		public FileInfo getInfo1() {
			return null;
			// TODO Auto-generated method stub
			
		}

		@Override
		public void getInfo() {
			// TODO Auto-generated method stub
			
		}
	}
}
