package com.oppo.transfer.sysf.wifidirect;

import com.oppo.transfer.ui.WifiTransferActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
	private static String TAG = "WiFiDirectBroadcastReceiver";
    private WifiP2pManager mManager;
    private Channel mChannel;
    //private Activity mActivity;
    private WifiDirectFunctions mWifiDirectFunctions;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
            IWifiDirectActionListener mActionListener) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        //this.mActivity = activity;
        this.mWifiDirectFunctions = new WifiDirectFunctions(mActionListener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
			Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
            // UI update to indicate wifi p2p status.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct mode is enabled
                //activity.setIsWifiP2pEnabled(true);
            } else {
                //activity.setIsWifiP2pEnabled(false);
                //activity.resetData();

            }
            Log.i(TAG, "P2P state changed - " + state);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
			Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
        	/*mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
				
				public void onPeersAvailable(WifiP2pDeviceList peers) {
					
					activity.displayPeers(peers);
					
				}
			});*/
			if (mManager != null ) {
				mManager.requestPeers(mChannel, mWifiDirectFunctions);
			} else {
				Log.e(TAG, "LEVEL 1 EXCEPTION");
			}
			Log.d(WifiTransferActivity.TAG, "P2P peers changed");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        	Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
            		.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                // we are connected with the other device, request connection
                // info to find group owner IP
                mManager.requestConnectionInfo(mChannel, mWifiDirectFunctions);
            } else {
                // It's a disconnect
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        	
        }
    }


}