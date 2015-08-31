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
	 * 创建文件
	 * @param fileName	文件名，如果是在SD下，直接为文件名加后缀，如果前面还有文件夹，需将文件夹也写入
	 * @return	创建后的文件
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName 目录名字，不用在后加/和后缀名
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断文件是否存在
	 * @param fileName	文件名，如果不是在根目录下，要将绝对路径名作为参数，如test文件夹下的test.txt，则为
	 * 			"test/test.txt"
	 * @return
	 */
	public static boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream写入SD卡中，注意文件路径和文件名，如在test文件夹下的test.txt，那么路径为"test/"，文件名为test.txt
	 * @param path	文件路径，最后必须加 /,前面不用加/
	 * @param fileName 文件名，记得加后缀名
	 * @param input 需要保存的流
	 * @return	返回一个保存好的文件
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