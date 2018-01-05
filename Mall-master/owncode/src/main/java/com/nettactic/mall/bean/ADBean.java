package com.nettactic.mall.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nettactic.mall.base.BaseBean;


public class ADBean  extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";
	private String image_url = "";
	private String sequence = "";
	private String url = "";
	private String title = "";
	private String icon_url = "";
	private String content = "";
	private String updatetime = "";
	private String onlinetime = "";
	private String offlinetime = "";
	private String tempstatus = "";
	private String share_url = "";
	
	public ADBean() {
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getOnlinetime() {
		return onlinetime;
	}
	public void setOnlinetime(String onlinetime) {
		this.onlinetime = onlinetime;
	}
	public String getOfflinetime() {
		return offlinetime;
	}
	public void setOfflinetime(String offlinetime) {
		this.offlinetime = offlinetime;
	}
	public String getTempstatus() {
		return tempstatus;
	}
	public void setTempstatus(String tempstatus) {
		this.tempstatus = tempstatus;
	}
	public String getShare_url() {
		return share_url;
	}
	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}
	@Override
	public String toString() {
		return "ADBean [id=" + id + ", image_url=" + image_url + ", sequence="
				+ sequence + ", url=" + url + ", title=" + title
				+ ", icon_url=" + icon_url + ", content=" + content
				+ ", updatetime=" + updatetime + ", onlinetime=" + onlinetime
				+ ", offlinetime=" + offlinetime + ", tempstatus=" + tempstatus
				+ ", share_url=" + share_url + "]";
	}
	public ADBean(String id, String image_url, String sequence, String url,
			String title, String icon_url, String content, String updatetime,
			String onlinetime, String offlinetime, String tempstatus,
			String share_url) {
		super();
		this.id = id;
		this.image_url = image_url;
		this.sequence = sequence;
		this.url = url;
		this.title = title;
		this.icon_url = icon_url;
		this.content = content;
		this.updatetime = updatetime;
		this.onlinetime = onlinetime;
		this.offlinetime = offlinetime;
		this.tempstatus = tempstatus;
		this.share_url = share_url;
	}
	
	public static List<ADBean> getDataToJson(JSONArray array) {
		List<ADBean> imageIdList = new ArrayList<ADBean>();
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				ADBean	ad = new ADBean();
				ad.setId(object.optString("id"));
				ad.setImage_url(object.optString("image_url"));
				ad.setSequence(object.optString("sequence"));
				ad.setUrl(object.optString("url"));
				ad.setTitle(object.optString("title"));
				ad.setIcon_url(object.optString("icon_url"));
				ad.setContent(object.optString("content"));
				ad.setUpdatetime(object.optString("updatetime"));
				ad.setOnlinetime(object.optString("onlinetime"));
				ad.setOfflinetime(object.optString("offlinetime"));
				ad.setTempstatus(object.optString("tempstatus"));
				ad.setShare_url(object.optString("share_url"));
				
				imageIdList.add(ad);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return imageIdList;
	}
	

}
