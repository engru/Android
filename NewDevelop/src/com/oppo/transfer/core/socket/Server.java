package com.oppo.transfer.core.socket;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.oppo.transfer.core.utils.Constants;

public class Server {
	ServerSocket server = null;
	//多线程
	private static List<Socket> list = new ArrayList<Socket>(); // 保存连接对象 
	private ExecutorService exec; 
	
	FileReceiver fr = null;
	
	public Server(){
		server = SocketUtils.getInstance().getServerInstance(null);
		listening();
	}

	private void listening() {
		// TODO Auto-generated method stub
		exec = Executors.newCachedThreadPool();
        while (true) {
            try {
				Socket client = server.accept();
				fr = new FileReceiver(client);/////////////////
	            if(false){
	                //sendFileToClient(socketToClient);
	                //socketToClient.close();
	            }else{
	                list.add(client);   
	                exec.execute(new Task(client));  
	            }

			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	
	class Task implements Runnable{
		Socket socket = null;

		public Task(Socket client) {
			// TODO Auto-generated constructor stub
			this.socket = client;
			//初始化的消息和操作
		}

		@Override
		public void run() {
			//sendFile(socket);
			try {
				receiveInfo();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void receiveInfo() throws IOException {
			// TODO Auto-generated method stub
			String CTR = null; 
			while(true){
				CTR = fr.getMsg();
				//是文件还是文件夹，
				if(CTR.equals(Constants.END) || CTR.equals(Constants.EXCEP)){
					//完成
					socket.close();
					return;
				}else if(CTR.equals(Constants.FILE)){
					ReceiveFile(socket);
				}else if(CTR.equals(Constants.DIR)){
					ReceiveFolder(socket);
				}else if(CTR.equals(Constants.DEFINED)){
					ReceiveDefined(socket);
				}
				//协商参数
				
				//正式传输
			}
		}
		
	}
	

	private void ReceiveFile(Socket socket){
		try {
			String str = fr.getMsg();
			if(str.equals(Constants.ONLYFILE)){
				fr.downloadFile();
			}else{
				fr.downloadFile(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	String rePath = "/sdcard/Storage/";
	private void ReceiveFolder(Socket socket){
		try {
			String path = fr.getMsg();
			if(path.equals(Constants.DIR))
				path = fr.getMsg();
			
			new File(rePath+path).mkdirs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ReceiveDefined(Socket socket){
		new File("Defined").mkdir();
	}
	
	/*
    private static final String fileToTransfer = "files/testing.xml";
    private void sendFile(Socket socketToClient) throws IOException {
        try {
            FileSender sender = new FileSender(socketToClient);
            sender.sendFile(fileToTransfer);
        } catch (Exception ex) {
            System.out.println("Error communicating with client: " + ex.getMessage());
        }

    }
	 */
}
