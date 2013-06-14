package com.oppo.transfer.ui;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.oppo.transfer.core.socket.Client;
import com.oppo.transfer.core.socket.Server;
import com.oppo.transfer.core.utils.Constants;
import com.oppo.transfer.core.utils.TransInfo;
import com.oppo.transfer.ui.lib.NotifyUtil;
import com.oppo.transfer.ui.lib.PopupWindowUtil;
import com.oppo.transfer.utils.SystemUtil;
import com.oppo.transfer.utils.WiFiDirectBroadcastReceiver;
import com.oppo.transfer.utils.WifiUtil;
import com.wkey.develop.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WifiTransferActivity extends BaseActivity implements PeerListListener ,ConnectionInfoListener {
	public static final String TAG = "WifiTransferActivity";
	private Context mContext;
	private Button mBtn_select;
	private Button mBtn_search;
	private ImageButton mBtn_send;
	private TextView mAppname;
	private TransInfo transInfo;
	
	private NotifyUtil mNotifyUtil;
	
	public static WifiUtil wifiUtil;
	private final IntentFilter intentFilter = new IntentFilter();
	private BroadcastReceiver receiver = null;
	private List<String> peernames = new ArrayList<String>();
	
	public WifiUtil getWifiUtil(){
		return wifiUtil;
	}
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mContext = this;
	    setContentView(R.layout.activity_wifi_transfer);
	    wifiUtil = WifiUtil.getInstance(this, getMainLooper());
	    mNotifyUtil = new NotifyUtil(this);
	    
	    
	    registerfilter();
	    
	    initViews();
	    initFunc();
	}
	
	private void registerfilter(){
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		//intentFilter
		//		.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
		
		receiver = new WiFiDirectBroadcastReceiver(wifiUtil.manager, wifiUtil.channel, this);
		registerReceiver(receiver, intentFilter);
	}
	
	ListView lv ;
    private void initViews() {
    	mBtn_select = (Button) findViewById(R.id.wifi_select_file);
    	mBtn_select.setOnClickListener(mBtnListener);
    	
    	mBtn_search = (Button) findViewById(R.id.wifi_search);
    	mBtn_search.setOnClickListener(mBtnListener);
    	
    	mBtn_send = (ImageButton) findViewById(R.id.btn_send);
    	mBtn_send.setOnClickListener(mBtnListener);
    	
    	mAppname = (TextView) findViewById(R.id.app_name_in_app);
    	mAppname.setOnClickListener(mBtnListener);
    	mAppname.setText("wifiTransfer:"+wifiUtil.getLocalIpAddress());
    	
    	peernames.add("调试");
    	adapter = new ArrayAdapter<String>(this, R.layout.list_item_wifi_transfer,R.id.device_name, peernames);
    	lv = (ListView)findViewById(R.id.wifi_device_list);
    	lv.setAdapter(adapter);
    	lv.setOnItemClickListener(mItemListener);
    }
    
    private void initFunc(){
    	transInfo = new TransInfo();
    	new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Server();
			}
    		
    	}).start();
    }
    
    
    
    ///////////////////////////////////////////////
    ArrayAdapter<String> adapter ;
    
    boolean isWifiP2pEnabled = true;
 
	public boolean onCreateOptionsMenu (Menu menu){
    			if (isWifiP2pEnabled) {

    				menu.add(Menu.FIRST, 99, 0,
    						"搜索")
    						.setEnabled(true)
    						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    			}
    			return true;
    	
    }
	
    private OnClickListener mBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.wifi_select_file:
					//select files to share
					Intent i = new Intent("oppo.intent.action.ACTION_FILEMANAGER_SELECT");
					startActivityForResult(i, 0);
					//change to onActivityResult()
					break;
				case R.id.wifi_search:
					//to do:
					discoverPeers();
					break;
				case R.id.app_name_in_app:
					mAppname.setText("wifiTransfer:"+wifiUtil.getLocalIpAddress());
					//PopupWindowUtil.showWindow(mContext, findViewById(R.id.app_name_in_app));
					PopupWindowUtil.show(mContext, findViewById(R.id.app_name_in_app));
					break;
				case R.id.btn_send:
					mNotifyUtil.IssueNotification();
			    	new Thread(new Runnable(){
			    		
						@Override
						public void run() {
							//String state = Constants.DIR;
							//String path = "/sdcard/sina";
							//List<String> paths = null;
							//Constants.P2P_IP = "";
							
						
							transInfo.setIP(Constants.P2P_IP);
							
							//new Client(state,path,paths);
							new Client(transInfo);
							
							if(true){
								for(int i = 1; i<11;i++){
									mNotifyUtil.updateNotification(i*10);
									try {
										new Thread().sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							
							
						}
			    	}).start();
					
					break;
			}
			
		}
		
    };
    
    private OnItemClickListener mItemListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			
			//showDetailInfo(((TextView)view.findViewById(R.id.device_name)).getText() );
			connect(position);
		}
    	
    };
    
    private void showDetailInfo(CharSequence device) {   
        new AlertDialog.Builder(this)   
        .setTitle("Device:" + device)               
        .setPositiveButton("确定", null)   
        .show();   
    }   
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	//super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode){
    	case 0:
    		//
    		//data.getdata
    		String Url = data.getStringExtra("SELECT_PATH");
    		System.out.println("onActivityresult:"+Url);
    		String Path = SystemUtil.getRealPathFromURL(mContext, Url);
    		//设置初始化 TransInfo 信息
    		transInfo.setPath(Path);
    		peernames.add(transInfo.getPath());
    		updateList();
    		//
    		
    		//data.getStringArrayExtra(name);
    		break;
    	case 1:
    		break;
    	}
    	
    }
    
    
    //search wifi p2p
    void discoverPeers(){
    	wifiUtil.manager.discoverPeers(wifiUtil.channel,discoverPeersActionListener );
    	
    }
    
    
	
	//open wifi direct
	void setWifiP2pEnable(){
		
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onpeer");
		if(peers != null){
			for (WifiP2pDevice remoteDevice : peers.getDeviceList()) {
				System.out.println(remoteDevice.deviceName +":"+ remoteDevice.status);
			}
			updatePeerList(peers);
		}
			
	}
	//private List peers = new ArrayList();
	List<WifiP2pDevice> peer = new ArrayList<WifiP2pDevice>();
	public void connect(int position) {
        // Picking the first device found on the network.
        WifiP2pDevice device = peer.get(position);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        wifiUtil.manager.connect(wifiUtil.channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WifiTransferActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

	private void updatePeerList(WifiP2pDeviceList peerList) {
        // Out with the old, in with the new.
       // peers.clear();
       // peers.addAll(peerList.getDeviceList());
		peer.clear();
		peer.addAll(peerList.getDeviceList());
        peernames.clear();

        	for(WifiP2pDevice remoteDevice : peerList.getDeviceList())
        		peernames.add(remoteDevice.deviceName);

        //peernames.add("test");
        
        // If an AdapterView is backed by this data, notify it
        // of the change.  For instance, if you have a ListView of available
        // peers, trigger an update.

				//adapter.notifyDataSetChanged();
				deviceHandler.sendEmptyMessage(100);
        
        if (peernames.size() == 0) {
            Log.d(TAG, "No devices found");
            return;
        }
	}

	 public void onConnectionInfoAvailable(final WifiP2pInfo info) {

	        // InetAddress from WifiP2pInfo struct.
	        //InetAddress groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

	        // After the group negotiation, we can determine the group owner.
	        if (info.groupFormed && info.isGroupOwner) {
	            // Do whatever tasks are specific to the group owner.
	            // One common case is creating a server thread and accepting
	            // incoming connections.
	        } else if (info.groupFormed) {
	            // The other device acts as the client. In this case,
	            // you'll want to create a client thread that connects to the group
	            // owner.
	        }
	}
	 
	public void updateList(){
		deviceHandler.sendEmptyMessage(100);
	}
	 
	Handler deviceHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100://WHAT_NOTIFY_GET_PRODUCTS:
				updateListView();
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
		
		private void updateListView() {
			adapter.notifyDataSetChanged();
		}
	};
	
	
	
	
}
