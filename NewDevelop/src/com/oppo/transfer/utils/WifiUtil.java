package com.oppo.transfer.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class WifiUtil {
	private static final String TAG = "WiFiUtil";
	private static WifiUtil uniqueInstance = null;
	private HandlerThread processThread;
	public WifiP2pManager manager;
	public Channel channel;
	//private WifiP2pManager.ActionListener wfdSettingStatuslistener;
	
	public static WifiUtil getInstance(Context context, Looper mainLooper) {
		if (uniqueInstance == null) {
			Log.e(TAG, "WifiUtil unique instance is null");
			uniqueInstance = new WifiUtil(context, mainLooper);
		}
		return uniqueInstance;
	}


	private WifiUtil(Context context, Looper mainLooper) {
		processThread = new HandlerThread("WiFiUtil");
		processThread.start();
		manager = (WifiP2pManager) context
				.getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(context, processThread.getLooper(), null);
		//manager.enableP2p(channel);
/*
		wfdSettingStatuslistener = new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				  Log.e(TAG,"WFD Settings Success");
				  Message messageInit = WFDClientActivity.eventHandler.
				  	obtainMessage(WFDClientActivity.setWfdFinished);
				  messageInit.arg1 = 0;
				  WFDClientActivity.eventHandler.sendMessage(messageInit);
			}

			@Override
			public void onFailure(int reasonCode) {
				  Log.e(TAG,"WFD Settings Failure");
				  Message messageInit = WFDClientActivity.eventHandler.
				  	obtainMessage(WFDClientActivity.setWfdFinished);
				  messageInit.arg1 = -1;
				  WFDClientActivity.eventHandler.sendMessage(messageInit);
			}
        };
        */
	}
	
	private static String peerIP;
	public static String getPeerIP(String peerMac) {
		Log.d(TAG, "getPeerIP() called....  peerMac=" + peerMac);

		if (peerIP != null) {
			return peerIP;
		} else {
			String ip = new String();

			/* Try getprop method */
			try {
				String line;
				java.lang.Process p = Runtime.getRuntime().exec(
						"getprop dhcp.wlan0.gateway");
				BufferedReader input = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					InetAddress gatewayAddr = InetAddress.getByName(line);
					gatewayAddr.isReachable(500);
				}
				input.close();
			} catch (Exception err) {
				Log.e(TAG, err.toString());
			}

			/* Try ARP table lookup */
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("/proc/net/arp"));
				String line;
				while ((line = br.readLine()) != null) {
					String[] splitted = line.split(" +");

					// consider it as a match if 3 out of 6 bytes of the mac
					// address match
					if (splitted != null && splitted.length >= 4) {
						String[] peerMacBytes = peerMac.split(":");
						String[] arpMacBytes = splitted[3].split(":");

						if (arpMacBytes == null) {
							continue;
						}

						int count = 0;
						for (int i = 0; i < arpMacBytes.length; i++) {
							if (peerMacBytes[i]
									.equalsIgnoreCase(arpMacBytes[i])) {
								count++;
							}
						}
						if (count >= 3) {
							// Basic sanity check
							ip = splitted[0];
							if (ip.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) {
								return ip;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return "";
	}
	
	public String getLocalIpV6Address() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
	        en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
	        enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(TAG, ex.toString());
	    }
	    return null;
	}
	
	
	public static String getLocalIpAddress(){
		
		try{
			 for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				 NetworkInterface intf = en.nextElement();  
	                for (Enumeration<InetAddress> enumIpAddr = intf  
	                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                    InetAddress inetAddress = enumIpAddr.nextElement();  
	                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {  
	                        
	                    	return inetAddress.getHostAddress().toString();  
	                    }  
	                }  
			 }
		}catch (SocketException e) {
			// TODO: handle exception
			Log.e(TAG,"WifiPreference IpAddress---error-" + e.toString());
		}
		
		return ""; 
	}
	
	/*
	public int getIpAddress() {
		 WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
		// ÅÐ¶ÏwifiÊÇ·ñ¿ªÆô
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return ipAddress;
	}
	
	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
			+ "." + (i >> 24 & 0xFF);
	}
	 */
}
