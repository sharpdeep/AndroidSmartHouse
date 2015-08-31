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
	 * 用appche的API，用Post方式提交数据到指定的URL
	 * @param urlStr	要提交的服务器地址
	 * @param content	提交的内容
	 * @param encode	编码方式，一般用utf-8
	 * @return	返回一个Inputstream
	 */
	public static InputStream postByHttp(String urlStr, Map<String, String> content, String encode){
		try {
			//封装键值对数据到请求体中
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(content != null && !content.isEmpty()){
				for (Map.Entry<String, String> entry : content.entrySet()) {
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encode);
				//新建一个HttpClient并完成Post操作等到一个HttpResponse对象，再由这个对象得到数据输入流
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(urlStr);
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				//如果打开成功
				if(response.getStatusLine().getStatusCode() == 200){
					//得到InputStream
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
	 * 通过appche的API从网络上Get到数据，并返回得到的输入流
	 * @param urlStr	指定的网络URL
	 * @return	若连接成功将会返回一个InputStream，否则返回null
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
	 * 将从网络上得到的InputStream保存为文件
	 * @param input	需要保存的InputStream，一般是Get或者Post之后得到的响应数据
	 * @param path	保存文件的路径，前面不需要加/,后面必须加/
	 * @param fileName 保存的文件名
	 * @param over 如果文件存在，是否覆盖
	 * @return	1：表示文件已经存在 	0：保存成功	-1 保存失败
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
