package com.oppo.transfer.sysf.wifi;

import java.util.ArrayList;
import java.util.List;

import com.oppo.transfer.utils.Constants;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
/**
 * Wifi Direct  的功能包括
 * 
 * WifiP2pManager.ActionListener			connect(), cancelConnect(), createGroup(), removeGroup(), and discoverPeers()
 * WifiP2pManager.ChannelListener			initialize()
 * WifiP2pManager.ConnectionInfoListener 	requestConnectInfo()
 * WifiP2pManager.GroupInfoListener			requestGroupInfo()
 * WifiP2pManager.PeerListListener			requestPeers()
 * @author 80054349
 *
 */

public class WifiFunctions implements PeerListListener ,ConnectionInfoListener{
	private IWifiActionListener mActionListener;
	public List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	
	public WifiFunctions(IWifiActionListener mActionListener){
		this.mActionListener = mActionListener;
	}
	

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		// TODO Auto-generated method stub
		//this.peers.clear();
		//this.peers.addAll(peers.getDeviceList());
		Constants.peers.clear();
		Constants.peers.addAll(peers.getDeviceList());
		//mActionListener.updatePeers(peers);
	}
	
	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// TODO Auto-generated method stub
		//mActionListener.updateConnectionInfo(info);
	}



}
