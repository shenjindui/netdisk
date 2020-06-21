package com.micro.netdisk.javasdk.transport;

public interface ITransport {
	/**
	 * 连接服务端
	 */
	public void start();
	/**
	 * 发送信息
	 * @param req
	 * @return
	 */
	public RpcResponse sendMsg(RpcRequest req);
	/**
	 * 断开服务端
	 */
	public void stop();
}
