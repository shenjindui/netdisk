package com.micro.netdisk.javasdk.transport;

import java.io.Serializable;

public class TcpRequest extends RpcRequest implements Serializable{
	private String className;//类名称
    private String methodName;//方法名称
    private Class[] types;//参数类型
    private Object[] params;//参数
    
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class[] getTypes() {
		return types;
	}
	public void setTypes(Class[] types) {
		this.types = types;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
}
