package com.micro.netdisk.javasdk.transport;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.micro.netdisk.javasdk.balance.LoadBalanceContext;
import com.micro.netdisk.javasdk.factory.FileFactory;

/**
 * 保证服务调用的稳定性
 * 1、动态感知
 * 2、失败重试3次
 * 3、接口幂等性
 * @author Administrator
 */
public class HttpTransport implements ITransport{
	private String projectname="netdisk-client-provider";
	
	@Override
	public void start() {
		//http无需处理
	}

	@Override
	public RpcResponse sendMsg(RpcRequest req) {
		CloseableHttpClient httpClient = null;  
        CloseableHttpResponse response = null;  
        RpcResponse res=new RpcResponse();
		try{
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			if(req instanceof HttpRequest){
				HttpRequest request=(HttpRequest)req;
				
				//负载均衡
				String host=LoadBalanceContext.select(FileFactory.activeHosts);
				if(host==null||"".equals(host)){
					throw new RuntimeException("服务器已经宕机,无法请求");
				}
				
				//普通参数
				entity.setCharset(Charset.forName("UTF-8"));
				Map<String,String> params=request.getParam();
				if(params!=null&&!params.isEmpty()){
					for(Map.Entry<String,String> entry:params.entrySet()){
						//ContentType.APPLICATION_JSON解决乱码问题
						entity.addTextBody(entry.getKey(),entry.getValue(),ContentType.APPLICATION_JSON);
					}
				}
				
				//文件参数
				//entity.addBinaryBody("file", file.getInputStream(), ContentType.parse(file.getContentType()),URLEncoder.encode(filename, "UTF-8"));
				//entity.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA,URLEncoder.encode(filename, "UTF-8"));
				//entity.addBinaryBody("file", file.getInputStream());
				if(request.getBytes()!=null&&request.getBytes().length!=0){			
					entity.addBinaryBody(request.getReceiveName(), request.getBytes(),ContentType.MULTIPART_FORM_DATA,request.getFileName());
				}
				
				//发送请求
				HttpPost post = new HttpPost("http://"+host+"/"+projectname+"/"+request.getRequrl());
				//post.addHeader("Content-type", "application/json; charset=utf-8");
				//post.addHeader("Accept", "application/json");
				post.setEntity(entity.build());
				httpClient = HttpClients.createDefault();
				response=httpClient.execute(post);
				HttpEntity httpEntity=response.getEntity();
				
				byte[] bytes=EntityUtils.toByteArray(httpEntity);
				//String json=EntityUtils.toString(httpEntity);
				
				res.setCode(0);
				res.setMsg("成功");
				res.setData(bytes);
			}else{
				throw new RuntimeException("类型不对");
			}
		}catch(Exception e){
			res.setCode(1);
			res.setMsg("失败："+e.getMessage());
		}finally{
			try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		}
		return res;
	}

	@Override
	public void stop() {
		//http无需处理		
	}

}
