package com.oppo.transfer.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.oppo.transfer.sysf.wifidirect.IWifiDirectActionListener;
import com.oppo.transfer.sysf.wifidirect.WiFiDirectBroadcastReceiver;
import com.oppo.transfer.utils.Constants;
import com.oppo.transfer.utils.WifiUtil;
import com.wkey.develop.R;

public class WindowNetworkSelect implements IWifiDirectActionListener {
	private static String TAG = "windowNetworkSelect";
	private IntentFilter intentFilter = new IntentFilter();
	private WiFiDirectBroadcastReceiver receiver;
	private WifiUtil wifiUtil;
	private Context mContext;
	
	private ListView mlistView;
	private ImageView iv;
	private TextView ssid,ip,peer_ip;
	private TabHost th ;
	
	private WifiP2pDevice mConnectedDevice = null;
	
	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	private ArrayAdapter<WifiP2pDevice> adapter;
	
	public WindowNetworkSelect(){
	}
	
	public WindowNetworkSelect(Context mContext){
		this.mContext = mContext;
	}
	
	public void init(Context mContext, View layout) {
		this.mContext = mContext;
		initFuncs();
		initViews(mContext,layout);
	}
	
	private void initFuncs() {
		//初始化、绑定 WifiDirectFunctions 和 WifiDirectBroadcastReceiver
		wifiUtil = WifiUtil.getInstance(mContext, null);
		registerfilter();
		
	}
	private void registerfilter(){
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		//intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
		
		receiver = new WiFiDirectBroadcastReceiver(wifiUtil.manager, wifiUtil.channel, this);
		mContext.registerReceiver(receiver, intentFilter);
	}
	
	
	private void initViews(Context mContext,View layout){
		//全局信息栏
		iv 	= (ImageView)layout.findViewById(R.id.network_type);
		ssid 	= (TextView)layout.findViewById(R.id.network_ssid);
		ip 	= (TextView)layout.findViewById(R.id.network_ip);
		peer_ip= (TextView)layout.findViewById(R.id.network_peer_ip);
		
		//TabHost 栏
		th = (TabHost)layout.findViewById(android.R.id.tabhost);
		th.setup();
		th.addTab(th.newTabSpec("type").setIndicator("wifi direct").setContent(R.id.tab1));
		th.addTab(th.newTabSpec("type").setIndicator("hotspot").setContent(R.id.tab2));
		th.addTab(th.newTabSpec("type").setIndicator("wifi").setContent(R.id.tab3));
		
		//
		//adapter = new ArrayAdapter<WifiP2pDevice>(this, R.layout.list_item_wifi_transfer,R.id.device_name, peernames);
		//ArrayAdapter<WifiP2pDevice> 
		adapter =new WiFiPeerListAdapter(mContext, R.layout.row_devices, Constants.peers);
		mlistView = (ListView)layout.findViewById(R.id.list_device);
		mlistView.setAdapter(adapter);
		
		//检测网络
		if(WifiUtil.checkNetwork(mContext)){
			System.out.println("network ok");
			//if(WifiUtil.isWifiWorking(mContext)){
			//	System.out.println("network wifi");
			//}else{
			//	
			//}
			//System.out.println(WifiUtil.getNetworkType(mContext));
		}else {
			//wifi direct
			
		}
		
		//wifi Direct
		
		
		
		
		
		ip.setText(WifiUtil.getLocalIpAddress());
		//peer_ip.setText(WifiUtil.getPeerIP(""));
	}
	
	/**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

        private List<WifiP2pDevice> items;

        /**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_devices, null);
            }
            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) v.findViewById(R.id.device_name);
                TextView bottom = (TextView) v.findViewById(R.id.device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                }
                if (bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                    /*----------------------*/
                    if(device.status==WifiP2pDevice.CONNECTED){
                    	mConnectedDevice = device;
                    	System.out.println("mconnecteddevice ok");
                    }
                    /*----------------------*/
                }
            }

            return v;

        }
        
        private String getDeviceStatus(int deviceStatus) {
            Log.d(TAG, "Peer status :" + deviceStatus);
            switch (deviceStatus) {
                case WifiP2pDevice.AVAILABLE:
                    return "Available";
                case WifiP2pDevice.INVITED:
                    return "Invited";
                case WifiP2pDevice.CONNECTED:
                    return "Connected";
                case WifiP2pDevice.FAILED:
                    return "Failed";
                case WifiP2pDevice.UNAVAILABLE:
                    return "Unavailable";
                default:
                    return "Unknown";

            }
        }
    }
    
	Handler deviceHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100://WHAT_NOTIFY_GET_PRODUCTS:
				updateListView();
				break;
			case 200:
				updateDeviceInfo();
				break;
			default:
				//if (isScrolling) {
					//hasNotify = true;
				//} else {
					//hasNotify = false;
					updateListView();
				//}
				break;
			}
		}
		
		private void updateDeviceInfo() {
			// TODO Auto-generated method stub
			ssid.setText(mConnectedDevice.deviceName);
			System.out.println(mConnectedDevice.deviceAddress+"&"+mConnectedDevice.deviceName);
			String peerip = WifiUtil.getPeerIP(mConnectedDevice.deviceAddress);
			peer_ip.setText(peerip);
			com.oppo.transfer.core.utils.Constants.transInfo.IP = peerip;
			
		}

		private void updateListView() {
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void updatelist() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePeers(WifiP2pDeviceList peers) {
		// TODO Auto-generated method stub
		//adapter.notifyDataSetChanged();
		deviceHandler.sendEmptyMessage(100);
		for(WifiP2pDevice device:peers.getDeviceList()){
            if(device.status==WifiP2pDevice.CONNECTED){
            	mConnectedDevice = device;
            	System.out.println("mconnecteddevice ok");
            }
		}
        if (Constants.peers.size() == 0) {
            Log.d(TAG, "No devices found");
            return;
        }
	}

	@Override
	public void updateConnectionInfo(WifiP2pInfo info) {
		// TODO Auto-generated method stub
		//info.
		System.out.println("updateConnectionInfo");
		if(mConnectedDevice!=null){

			deviceHandler.sendEmptyMessage(200);
		}
	}




}
