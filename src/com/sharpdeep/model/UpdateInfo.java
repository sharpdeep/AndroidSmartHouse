package com.sharpdeep.model;
/**
 * �˴��õ���ģʽ
 * @author Bear
 *
 */
public class UpdateInfo {

	private String updateUrl = "";//apk���ص�ַ
	private String version = "1.0";//�汾
	private String description = "";//������
	private boolean needUpdate = false;
	
	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public static UpdateInfo getUpdateInfo() {
		return updateInfo;
	}

	public static void setUpdateInfo(UpdateInfo updateInfo) {
		UpdateInfo.updateInfo = updateInfo;
	}

	private UpdateInfo(){
		
	}
	
	private static UpdateInfo updateInfo = new UpdateInfo();
	
	public static UpdateInfo newInstance(){
		return updateInfo;
	}
	
	public String getUpdateUrl() {
		return updateUrl;
	}
	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
