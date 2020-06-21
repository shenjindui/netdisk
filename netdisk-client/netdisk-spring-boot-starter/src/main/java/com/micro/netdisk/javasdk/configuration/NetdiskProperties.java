package com.micro.netdisk.javasdk.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="netdisk.server")
public class NetdiskProperties {
	private String host;

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
}
