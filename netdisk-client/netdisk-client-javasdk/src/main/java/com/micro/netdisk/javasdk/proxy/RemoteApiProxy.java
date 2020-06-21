package com.micro.netdisk.javasdk.proxy;

public class RemoteApiProxy {
	private volatile static RemoteApi remoteApi;
	
	public static RemoteApi getInstance(){
		if(remoteApi==null){
			synchronized (RemoteApiProxy.class) {
				if(remoteApi==null){
					remoteApi=new RemoteApiImpl();
				}
			}
		}
		return remoteApi;
	}
}
