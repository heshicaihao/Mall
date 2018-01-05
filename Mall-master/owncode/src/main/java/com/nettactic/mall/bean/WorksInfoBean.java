package com.nettactic.mall.bean;

import java.util.Comparator;

import com.nettactic.mall.base.BaseBean;

public class WorksInfoBean extends BaseBean implements
		Comparator<WorksInfoBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String workid;
	private String jsonstring;
	private String goodsname;
	private String quantity = "1";
	private String price;
	private String imageurl;
	private String goodsid;
	private String type;
	private String picurl;
	private String productid;
	private String userid;
	private String editstate;// 0 可编辑 1 不可编辑
	private String lasttime;

	public WorksInfoBean() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public String getJsonstring() {
		return jsonstring;
	}

	public void setJsonstring(String jsonstring) {
		this.jsonstring = jsonstring;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getEditstate() {
		return editstate;
	}

	public void setEditstate(String editstate) {
		this.editstate = editstate;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	@Override
	public String toString() {
		return "WorksInfoBean [id=" + id + ", workid=" + workid
				+ ", jsonstring=" + jsonstring + ", goodsname=" + goodsname
				+ ", quantity=" + quantity + ", price=" + price + ", imageurl="
				+ imageurl + ", goodsid=" + goodsid + ", type=" + type
				+ ", picurl=" + picurl + ", productid=" + productid
				+ ", userid=" + userid + ", editstate=" + editstate
				+ ", lasttime=" + lasttime + "]";
	}

	public WorksInfoBean(String workid, String jsonstring, String goodsname,
			String quantity, String price, String imageurl, String goodsid,
			String type, String picurl, String productid, String userid,
			String editstate, String lasttime) {
		super();
		this.workid = workid;
		this.jsonstring = jsonstring;
		this.goodsname = goodsname;
		this.quantity = quantity;
		this.price = price;
		this.imageurl = imageurl;
		this.goodsid = goodsid;
		this.type = type;
		this.picurl = picurl;
		this.productid = productid;
		this.userid = userid;
		this.editstate = editstate;
		this.lasttime = lasttime;
	}

	@Override
	public int compare(WorksInfoBean bean0, WorksInfoBean bean1) {
		int num = 0;
		String timeStr0 = bean0.getWorkid();
		int time0 = Integer.parseInt(timeStr0);

		String timeStr1 = bean1.getWorkid();
		int time1 = Integer.parseInt(timeStr1);
		int value = time0 - time1;
		if (value > 0) {
			num = -1;
		} else if (value == 0) {
			num = 0;
		} else if (value < 0) {
			num = 1;
		}
		return num;
	}

}
