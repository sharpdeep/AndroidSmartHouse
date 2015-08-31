package com.sharpdeep.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {
	
	/**
	 * ��appche��API����Post��ʽ�ύ���ݵ�ָ����URL
	 * @param urlStr	Ҫ�ύ�ķ�������ַ
	 * @param content	�ύ������
	 * @param encode	���뷽ʽ��һ����utf-8
	 * @return	����һ��Inputstream
	 */
	public static InputStream postByHttp(String urlStr, Map<String, String> content, String encode){
		try {
			//��װ��ֵ�����ݵ���������
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(content != null && !content.isEmpty()){
				for (Map.Entry<String, String> entry : content.entrySet()) {
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encode);
				//�½�һ��HttpClient�����Post�����ȵ�һ��HttpResponse���������������õ�����������
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(urlStr);
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				//����򿪳ɹ�
				if(response.getStatusLine().getStatusCode() == 200){
					//�õ�InputStream
					InputStream input = response.getEntity().getContent();
					return input;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	/**
	 * ͨ��appche��API��������Get�����ݣ������صõ���������
	 * @param urlStr	ָ��������URL
	 * @return	�����ӳɹ����᷵��һ��InputStream�����򷵻�null
	 */
	public static InputStream getByHttp(String urlStr){
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(urlStr);
		try {
			HttpResponse response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == 200){
				InputStream input = response.getEntity().getContent();
				return input;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	/**
	 * ���������ϵõ���InputStream����Ϊ�ļ�
	 * @param input	��Ҫ�����InputStream��һ����Get����Post֮��õ�����Ӧ����
	 * @param path	�����ļ���·����ǰ�治��Ҫ��/,��������/
	 * @param fileName ������ļ���
	 * @param over ����ļ����ڣ��Ƿ񸲸�
	 * @return	1����ʾ�ļ��Ѿ����� 	0������ɹ�	-1 ����ʧ��
	 */
	public static int saveFromInputStream(InputStream input, String path, String fileName, boolean over){
		FileUtils fileUtils = new FileUtils();
		if(fileUtils.isFileExist(path+fileName)){
			if(!over){
				return 1;
			}
		}
		else{
			File file = fileUtils.write2SDFromInput(path, fileName, input);
			if(file == null){
				return -1;
			}
		}
		return 0;
	}
	
	public String getStringFromInputStream(InputStream input){
		String line = null;
		StringBuffer sb = new StringBuffer();
		BufferedReader buffer = null;
		
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			while((line = buffer.readLine()) != null){
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}
}
