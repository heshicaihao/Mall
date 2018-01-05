package com.nettactic.mall.bean;

import com.nettactic.mall.base.BaseBean;


public class ShoppingInfoBean extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String obj_ident;
	private String quantity;
	private String work_id;

	public String getObj_ident() {
		return obj_ident;
	}

	public void setObj_ident(String obj_ident) {
		this.obj_ident = obj_ident;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	@Override
	public String toString() {
		return "ShoppingInfoBean [obj_ident=" + obj_ident + ", quantity="
				+ quantity + ", work_id=" + work_id + "]";
	}

	public ShoppingInfoBean(String obj_ident, String quantity, String work_id) {
		super();
		this.obj_ident = obj_ident;
		this.quantity = quantity;
		this.work_id = work_id;
	}

	public ShoppingInfoBean() {
		super();
	}

}
