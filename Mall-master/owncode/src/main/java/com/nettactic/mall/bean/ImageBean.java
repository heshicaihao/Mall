package com.nettactic.mall.bean;

import java.util.List;

import com.nettactic.mall.base.BaseBean;


public class ImageBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String >  Paths;
	private String TopImagePath;
	private String FolderName;
	private int ImageCount;
	private String Data;
	
	public List<String> getPaths() {
		return Paths;
	}
	public void setPaths(List<String> paths) {
		Paths = paths;
	}
	public String getTopImagePath() {
		return TopImagePath;
	}
	public void setTopImagePath(String topImagePath) {
		TopImagePath = topImagePath;
	}
	public String getFolderName() {
		return FolderName;
	}
	public void setFolderName(String folderName) {
		FolderName = folderName;
	}
	public int getImageCount() {
		return ImageCount;
	}
	public void setImageCount(int imageCount) {
		ImageCount = imageCount;
	}
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	



}