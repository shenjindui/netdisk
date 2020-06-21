package com.micro.netdisk.javasdk.transport;

import com.micro.netdisk.javasdk.factory.FileFactory;
import com.micro.netdisk.javasdk.factory.TransprotMode;

public class TransportContext {
	private volatile static ITransport transport;
	/**
	 * 连接服务端
	 */
	public static void start(){
		if(transport==null){
			synchronized (TransportContext.class) {
				if(transport==null){					
					String mode=FileFactory.mode;
					if(mode.equals(TransprotMode.HTTP_MODE)){
						transport=new HttpTransport();
						
					}else if(mode.equals(TransprotMode.TCP_MODE)){
						transport=new TcpTransport();
					}
				}
			}
		}
		transport.start();
	}
	/**
	 * 发送信息
	 * @param req
	 * @return
	 */
	public static RpcResponse sendMsg(RpcRequest req){
		if(transport==null){
			synchronized (TransportContext.class) {
				if(transport==null){					
					String mode=FileFactory.mode;
					if(mode.equals(TransprotMode.HTTP_MODE)){
						transport=new HttpTransport();
						
					}else if(mode.equals(TransprotMode.TCP_MODE)){
						transport=new TcpTransport();
					}
				}
			}
		}
		return transport.sendMsg(req);
	}
	
	/**
	 * 断开服务端
	 */
	public static void stop(){
		if(transport==null){
			synchronized (TransportContext.class) {
				if(transport==null){					
					String mode=FileFactory.mode;
					if(mode.equals(TransprotMode.HTTP_MODE)){
						transport=new HttpTransport();
						
					}else if(mode.equals(TransprotMode.TCP_MODE)){
						transport=new TcpTransport();
					}
				}
			}
		}
		transport.stop();
	}
}
