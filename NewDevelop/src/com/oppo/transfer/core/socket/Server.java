package com.oppo.transfer.core.socket;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

import com.oppo.transfer.core.utils.Constants;
import com.oppo.transfer.core.utils.StateListener;
import com.oppo.transfer.core.utils.TransStateMachine;
import com.oppo.transfer.ui.WifiTransferActivity;

public class Server {
	Context mContext;
	ServerSocket server = null;
	//多线程
	private static List<Socket> list = new ArrayList<Socket>(); // 保存连接对象 
	private ExecutorService exec; 
	
	FileReceiver fr = null;
	TransStateMachine mStateMachine;
	
	public Server(){
		server = SocketUtils.getInstance().getServerInstance(null);
		listening();
	}
	
	public Server(Context mContext){
		this.mContext = mContext;
		server = SocketUtils.getInstance().getServerInstance(null);
		listening();
	}
	
	public Server(Context mContext,StateListener sl){
		this.mContext = mContext;
		mStateMachine = new TransStateMachine();
		mStateMachine.registerSateListener(sl);
		
		server = SocketUtils.getInstance().getServerInstance(null);
		listening();
	}

	private void listening() {
		// TODO Auto-generated method stub
		exec = Executors.newCachedThreadPool();
        while (true) {
            try {
				Socket client = server.accept();
				fr = new FileReceiver(client,mStateMachine);/////////////////
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
			mStateMachine.setState(TransStateMachine.Negotiate);
			getSendReq();
			/*String CTR = null; 
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
			}*/
		}

		private void getSendReq() {
			// TODO Auto-generated method stub
			try {
				String req = fr.getMsg();
				if(req.equals(Constants.REQSEND)){
					//dialog
					Looper.prepare();
					if(mContext!=null){
						System.out.println("mcontext "+mContext.toString());
					}else{
						System.out.println("mcontext null");
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("收到发送请求").setTitle("是否接收?");
					builder.setPositiveButton("确认接收", new DialogInterface.OnClickListener() { 
				        @Override 
				        public void onClick(DialogInterface dialog, int which) { 
					        // 点击“确认”后的操作 
				        	//返回确认
				        	dialog.dismiss();
				        	new Thread(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
						        	fr.sendMsg(Constants.ALLOW);
						        	try {
										receiveContent();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
				        		
				        	}).start();


				        } 
				    });
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
				        @Override 
				        public void onClick(DialogInterface dialog, int which) {
				        	fr.sendMsg(Constants.DENY);
				        	try {
								if(fr.getMsg().equals(Constants.END))
									receiveComplete();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	dialog.dismiss();
				        } 
				    });
					
					AlertDialog ad = builder.create();
					ad.show();
					Looper.loop();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void receiveContent() throws IOException{
			mStateMachine.setState(TransStateMachine.Transfer);
			String CTR = null;
			while(true){
				CTR = fr.getMsg();
				//是文件还是文件夹，
				if(CTR == null){
					receiveComplete();
					return;
				}else if(CTR.equals(Constants.END) || CTR.equals(Constants.EXCEP)){
					//完成
					receiveComplete();
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
		
		private void receiveComplete() throws IOException{
			socket.close();
        	mStateMachine.setState(TransStateMachine.Complete);
		}
		
	}
	

	private void ReceiveFile(Socket socket){
		try {
			String str = fr.getMsg();
			System.out.println("receiverFile:begain");
			if(str.equals(Constants.ONLYFILE)){
				fr.downloadFile();
			}else{
				fr.downloadFile(str);
			}
			System.out.println("receiverFile:end");
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
