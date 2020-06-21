package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSecretRequest;
import com.micro.common.ValidateUtils;

@Component
public class ShareSecretValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSecretRequest){
			ShareSecretRequest bean=(ShareSecretRequest) request;
			
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择需要分享的记录");
			}
			ValidateUtils.validate(bean.getTitle(), "分享标题");
			ValidateUtils.validate(bean.getUserid(), "分享人ID");
			ValidateUtils.validate(bean.getUsername(), "分享人姓名");
			ValidateUtils.validate(bean.getSharetype(), "分享形式");
			ValidateUtils.validate(bean.getEffect(), "有效期");
			ValidateUtils.validate(bean.getType(), "分享来源");
			
			if(bean.getSharetype()!=0&&bean.getSharetype()!=1){//【属于私密分享】0有提取码，1无提取码
				throw new RuntimeException("分享形式格式不对");
			}
			if(bean.getEffect()!=0&&bean.getEffect()!=7&&bean.getEffect()!=1){//【属于私密分享】0永久，7表示7天，1表示1天
				throw new RuntimeException("有效期格式不对");
			}
			if(bean.getType()!=0&&bean.getType()!=1){
				throw new RuntimeException("分享来源格式不对");				
			}
		}else{
			throw new RuntimeException("ShareSecretValidateHandler==参数不对");
		}
	}

}
