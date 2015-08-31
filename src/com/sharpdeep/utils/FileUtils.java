package com.sharpdeep.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private static final String SDPATH = Environment.getExternalStorageDirectory() + "/";

	public String getSDPATH() {
		return SDPATH;
	}
	/**
	 * �����ļ�
	 * @param fileName	�ļ������������SD�£�ֱ��Ϊ�ļ����Ӻ�׺�����ǰ�滹���ļ��У��轫�ļ���Ҳд��
	 * @return	��������ļ�
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName Ŀ¼���֣������ں��/�ͺ�׺��
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * �ж��ļ��Ƿ����
	 * @param fileName	�ļ�������������ڸ�Ŀ¼�£�Ҫ������·������Ϊ��������test�ļ����µ�test.txt����Ϊ
	 * 			"test/test.txt"
	 * @return
	 */
	public static boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStreamд��SD���У�ע���ļ�·�����ļ���������test�ļ����µ�test.txt����ô·��Ϊ"test/"���ļ���Ϊtest.txt
	 * @param path	�ļ�·����������� /,ǰ�治�ü�/
	 * @param fileName �ļ������ǵüӺ�׺��
	 * @param input ��Ҫ�������
	 * @return	����һ������õ��ļ�
	 */
	public static File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			while((input.read(buffer)) != -1){
				output.write(buffer);
			}
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

}