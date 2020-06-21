package com.micro.logs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * http通讯工具类
 * @author zwy
 * 2018年9月12日
 */
public class HttpClientUtils{
	public static String doPost(String path,Map<String,String> params){
		String result="";
		try{			
			List<NameValuePair> pairs=new ArrayList<NameValuePair>();
			if(params!=null&&!params.isEmpty()){
				for(Map.Entry<String,String> entry:params.entrySet()){
					pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
				}
			}
			UrlEncodedFormEntity entity=new UrlEncodedFormEntity(pairs,HTTP.UTF_8);
			//StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			//Content-Type: application/json;charset=UTF-8
			
			HttpPost httppost=new HttpPost(path);
			httppost.setEntity(entity);
			
			CloseableHttpClient  client= HttpClients.createDefault();
			//DefaultHttpClient client=new DefaultHttpClient();
			
			HttpResponse response=client.execute(httppost);
			/*if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity());
			}else{
				result="neterror";
			}*/
			result = EntityUtils.toString(response.getEntity());
		}catch(Exception e){
			e.printStackTrace();
			result="neterror";
		}
		return result;
	}
}
