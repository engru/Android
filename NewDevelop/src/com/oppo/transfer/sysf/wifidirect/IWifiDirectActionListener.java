package com.oppo.transfer.sysf.wifidirect;

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;

public interface IWifiDirectActionListener {
	void updatePeers(WifiP2pDeviceList peers);
	void updateConnectionInfo(WifiP2pInfo info);
	
	void updatelist();
	void test();
}
