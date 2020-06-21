package com.micro.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.micro.search.bean.FileSearchBean;
import com.micro.search.bean.Page;
import com.micro.search.service.FileSearchService;

@Component(value="Solr")
public class FileSearchSolrServiceImpl implements FileSearchService{
	@Autowired
	private SolrClient solrClient;
	
	public static void main(String[] args) throws SolrServerException, IOException {
		SolrClient sc=new HttpSolrClient("http://192.168.1.8:8983/solr/disk");
		//SolrClient sc=new HttpSolrClient("http://106.15.248.223:8983/solr/disk");
		sc.deleteByQuery("*:*");
		sc.commit();
		System.err.println("执行完成");
	}
	
	@Override
	public Page<FileSearchBean> search(String filename,String userid, Integer page, Integer limit) {
		Page<FileSearchBean> pageInfo=new Page<FileSearchBean>();
		try{			
			//创建一个SolrQuery对象
			SolrQuery query = new SolrQuery();
			//条件
			if(StringUtils.isEmpty(filename)){				
				query.setQuery("pid:0");
				query.setFilterQueries("createuserid:"+userid+"");
			}else{				
				query.setQuery("filename:"+filename+"");
				query.setFilterQueries("createuserid:"+userid+"");
			}
			//分页
			page=page==null?1:page;
			limit=limit==null?10:limit;
			int first=(page-1)*limit;
			query.setStart(first);
			query.setRows(limit);
			
			//排序
			query.setSort("createtime", SolrQuery.ORDER.desc);
			//高亮
			query.setHighlight(true);
			query.addHighlightField("filename");
			query.setHighlightSimplePre("<span style=\"color:red;\">");
			query.setHighlightSimplePost("</span>"); 
			
			//查询结果
			//QueryResponse response =solrClient.query(query);
			QueryResponse response =solrClient.query("disk",query);

			//取查询结果
			SolrDocumentList solrDocumentList = response.getResults();
			//取查询结果总记录数
			long count=solrDocumentList.getNumFound();
			long pageCount=(count%limit==0)?(count/limit):(count/limit+1);
			List<FileSearchBean> rows=getRows(solrDocumentList, response);
			
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setRows(rows);
			pageInfo.setTotalElements(count);
			pageInfo.setTotalPage(pageCount);
			
			pageInfo.setCode(0);
			pageInfo.setMsg("查询成功");
			return pageInfo;
		}catch(Exception e){
			e.printStackTrace();
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
	
	public List<FileSearchBean> getRows(SolrDocumentList solrDocumentList,QueryResponse response){
		List<FileSearchBean> rows=new ArrayList<FileSearchBean>();
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			String id=solrDocument.get("id")==null?"":solrDocument.get("id").toString();
			String pid=solrDocument.get("pid")==null?"":solrDocument.get("pid").toString();
			String pname=solrDocument.get("pname")==null?"":solrDocument.get("pname").toString();
			String filemd5=solrDocument.get("filemd5")==null?"":solrDocument.get("filemd5").toString();
			String fileicon=solrDocument.get("fileicon")==null?"":solrDocument.get("fileicon").toString();
			String typecode=solrDocument.get("typecode")==null?"":solrDocument.get("typecode").toString();
			String filesuffix=solrDocument.get("filesuffix")==null?"":solrDocument.get("filesuffix").toString();
			String filesize=solrDocument.get("filesize")==null?"":solrDocument.get("filesize").toString();
			String filetype=solrDocument.get("filetype")==null?"":solrDocument.get("filetype").toString();
			String createuserid=solrDocument.get("createuserid")==null?"":solrDocument.get("createuserid").toString();
			String createusername=solrDocument.get("createusername")==null?"":solrDocument.get("createusername").toString();
			String createtime=solrDocument.get("createtime")==null?"":solrDocument.get("createtime").toString();
			
			//取高亮显示
			List<String> list = highlighting.get(id).get("filename");
			String title = "";
			if (list != null && list.size() >0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("filename");
			}
			FileSearchBean row=new FileSearchBean();
			row.setId(id);
			row.setFilename(title);
			row.setPid(pid);
			row.setPname(pname);
			row.setFilemd5(filemd5);
			row.setFileicon(fileicon);
			row.setTypecode(typecode);
			row.setFilesuffix(filesuffix);
			row.setFilesize(filesize);
			row.setFiletype(filetype);
			row.setCreateuserid(createuserid);
			row.setCreateusername(createusername);
			row.setCreatetime(createtime);
			
			rows.add(row);
		}
		return rows;
	}

	@Override
	public void add(FileSearchBean bean) {
		try{			
			SolrInputDocument document=new SolrInputDocument();
			document.addField("id",bean.getId());
			document.addField("filename",bean.getFilename());
			document.addField("pid",bean.getPid());
			document.addField("pname", bean.getPname());
			document.addField("filemd5",bean.getFilemd5());
			document.addField("fileicon",bean.getFileicon());
			document.addField("typecode",bean.getTypecode());
			document.addField("filesuffix",bean.getFilesuffix());
			document.addField("filesize",bean.getFilesize());
			document.addField("filetype",bean.getFiletype());
			document.addField("createuserid",bean.getCreateuserid());
			document.addField("createusername",bean.getCreateusername());
			document.addField("createtime",bean.getCreatetime());
			
			solrClient.add("netdisk", document);
			solrClient.commit("netdisk");
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void delete(String id) {
		try{
			solrClient.deleteById("disk", id);
			solrClient.commit("disk");
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public void deleteAll() {
		try{			
			solrClient.deleteByQuery("disk","*:*");
			solrClient.commit("disk");		
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public Long findCount() {
		try{			
			//创建一个SolrQuery对象
			SolrQuery query = new SolrQuery();
			query.setQuery("*:*");
			//查询结果
			//QueryResponse response =solrClient.query(query);
			QueryResponse response =solrClient.query("disk",query);
			return response.getResults().getNumFound();
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
}
