package com.micro.common.json;

import java.util.List;

public interface JsonUtils {
	public String objectToJson(Object data);
	public <T> T jsonToPojo(String jsonData, Class<T> beanType);
	public <T>List<T> jsonToList(String jsonData, Class<T> beanType);
}
