package com.oppo.transfer.core.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.oppo.transfer.core.utils.Constants;
import com.oppo.transfer.ui.WifiTransferActivity;
import com.oppo.transfer.utils.WifiUtil;

public class SocketUtils {
	private static final String TAG = "SocketUtils";
	private static SocketUtils Instance = null;
	private static ServerSocket ServerInstance = null;
	//private static Socket ClientInstance = null;
	private static int communicationPort = Constants.DEFAULT_PORT;//13267;
	
	
	
	public static SocketUtils getInstance() {
		if (Instance == null) {
			Instance = new SocketUtils();
		}
		return Instance;
	}
	
	private SocketUtils(){
		
	}
	
	//server
	public ServerSocket getServerInstance(Context context) {
		if (ServerInstance == null) {
			ServerInstance = createServer(context);
		}
		return ServerInstance;
	}

	private ServerSocket createServer(Context context) {
		// TODO Auto-generated method stub
		try {
			return new ServerSocket(communicationPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	//client
	public Socket getClientInstance(Context mContext) {
		/*if (ClientInstance == null) {
			ClientInstance = createClient(mContext);
		}
		return ClientInstance;
		*/
		return createClient(mContext);
	}
	
	public Socket getClientInstance(String IP,int Port) {
		/*if (ClientInstance == null) {
			ClientInstance = createClient(IP,Port);
		}
		return ClientInstance;
		*/
		return createClient(IP, Port);
	}

	private Socket createClient(String IP,int Port) {
		// TODO Auto-generated method stub
		//String serverIPAddress ;//= WifiTransferActivity.wifiUtil.getPeerIP("");
		//serverIPAddress = Constants.P2P_IP;
		
		try {
			
			return new Socket(IP, Port);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	private Socket createClient(Context context) {
		// TODO Auto-generated method stub
		String serverIPAddress ;//= WifiTransferActivity.wifiUtil.getPeerIP("");
		serverIPAddress = Constants.P2P_IP;
		
		try {
			
			return new Socket(serverIPAddress, communicationPort);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
