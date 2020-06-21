package com.micro.search.service.impl;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class TestSolr {

	public static void main(String[] args) throws SolrServerException, IOException {
		SolrClient solrClient=new HttpSolrClient("http://192.168.1.8:8983/solr/disk");
		solrClient.deleteByQuery("*:*");
		solrClient.commit();
		
		/*SolrInputDocument document=new SolrInputDocument();
        document.addField("id","2");
        document.addField("filename","你好.doc");
        document.addField("filemd5","abc");
        solrClient.add(document);
        solrClient.commit();		
        
        System.out.println("保存Solr成功..............");*/
		
		
		
	/*	SolrQuery query = new SolrQuery();
		query.setQuery("filename:*");
		QueryResponse response =solrClient.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		 for (SolrDocument doc : solrDocumentList) {
			 String id=doc.get("id")==null?"":doc.get("id").toString();
			 String filename=doc.get("filename")==null?"":doc.get("filename").toString();
			 String filemd5=doc.get("filemd5")==null?"":doc.get("filemd5").toString();
			 
			 System.out.println("输出结果："+id+"..."+filename+"..."+filemd5);
		 }*/
	}
}
