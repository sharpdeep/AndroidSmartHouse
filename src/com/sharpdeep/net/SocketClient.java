package com.sharpdeep.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.http.util.ByteArrayBuffer;

public class SocketClient{
	
	public static final String DEFAULT_URL = "192.168.1.101";
	public static final int DEFAULT_PORT = 2001;
	
	private static final int BYTE_NUM = 10;//�����ֽ����鳤��
	private static final int MAX_NUM = 1024;
	private static final int TIME_OUT = 5000;
	
	private InputStream mSocketInput = null;
	private OutputStream mSocketOutput = null;
	
	private Socket mSocket;
	
	private SocketClient(){
		
	}
	
	private static SocketClient socketClient;
	
	public static synchronized SocketClient getInstance(){
		if(socketClient == null){
			socketClient = new SocketClient();
		}
		return socketClient;
	}
	/**
	 * -2 socket�ر��쳣��-1 socket���ӳ�ʱ�� 1 ���ӳɹ�
	 * @param host
	 * @param port
	 * @return
	 */
	public int connect(String host, int port){
		if(null == mSocket){
			mSocket = new Socket();
		}
				
		try {
			if(!mSocket.isConnected()){
				InetSocketAddress addr = new InetSocketAddress(host, port);
				mSocket.setKeepAlive(true);
				mSocket.connect(addr, TIME_OUT);
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				mSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return -2;
			}
			return -1;
		}
		return 1;
	}
	
	/**
	 * 1  �ɹ���  -1 ʧ��
	 * @return
	 */
	public int disconnet(){
		if(null != mSocket){
			try {
//				mSocketInput.close();
//				mSocketOutput.close();
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 1;
	}
	
	/**
	 * �����ֽ����飬����ǰȷ��socket�Ѿ�����
	 * 1 �ɹ� -1 ʧ��
	 * @param buffer
	 */
	public int sendMessage(byte[] buffer){
		if(mSocket != null){
			try {
				mSocketOutput = mSocket.getOutputStream();
				mSocketOutput.write(buffer);
				mSocketOutput.flush();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 1;
	}
	
	/**
	 * ��ȡ���ݣ�ͨ��byte���鷵�أ����Ϊnull��˵����ȡʧ��
	 * @return
	 */
	public byte[] getMessage(){
		ByteArrayBuffer buffer = new ByteArrayBuffer(MAX_NUM);
		byte[] bytes = new byte[MAX_NUM];
		if(mSocket != null){
			try {
				mSocketInput = mSocket.getInputStream();
				int len = mSocketInput.read(bytes);
				buffer.append(bytes, 0, len);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return buffer.toByteArray();
	}
	
	public boolean isConnect(){
		if(mSocket != null){
			return mSocket.isConnected() ? true : false;
		}
		return false;
	}
	
	public boolean isClose(){
		if(mSocket != null){
			return mSocket.isClosed() ? true : false;
		}
		return true;
	}
}
