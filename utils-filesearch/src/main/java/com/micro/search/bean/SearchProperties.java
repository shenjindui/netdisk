package com.micro.search.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="search.server")
public class SearchProperties {
	private String url;
}
