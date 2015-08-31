package com.sharpdeep.utils;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.sharpdeep.data.Constant;
import com.sharpdeep.model.UpdateInfo;

public class XMLParser {
	
	
	/**
	 * ����XML�ĵ�Ϊ��Ҫ����Ϣ������װ,����-1��ʾʧ�ܣ�����0��ʾ�ɹ�
	 * @param is
	 * @return
	 */
	public static int parseXML2Info(InputStream is){
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			String version = doc.getElementsByTagName("version").item(0).getTextContent();
			String updateUrl = doc.getElementsByTagName("updateurl").item(0).getTextContent();
			String description = doc.getElementsByTagName("description").item(0).getTextContent();
			
			UpdateInfo updateInfo = UpdateInfo.newInstance();
			updateInfo.setVersion(version);
			updateInfo.setUpdateUrl(updateUrl);
			updateInfo.setDescription(description);
			
			return Constant.XML_PARSE_SUCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.XML_PARSE_FAILED;
		}
	}
	
}
