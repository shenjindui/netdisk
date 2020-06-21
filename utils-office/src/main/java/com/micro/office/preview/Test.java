package com.micro.office.preview;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

public class Test {
	public static void main(String[] args) throws Exception {
		PreviewService ps=new AsposePreviewServiceImpl();
		String path="E:/jar/Jenkins.docx";
		FileInputStream input=new FileInputStream(path);
		
		byte[] bytes=IOUtils.toByteArray(input);
		byte[] pdfbytes=ps.converToPdf("", bytes);
		
		FileOutputStream output=new FileOutputStream("E:/jar/Jenkins.pdf");
		output.write(pdfbytes);
		output.flush();
		
		input.close();
		output.close();
		
		System.out.println("..................................");
	}
}
