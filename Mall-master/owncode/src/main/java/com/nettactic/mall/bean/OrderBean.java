package com.nettactic.mall.bean;

import com.nettactic.mall.base.BaseBean;


public class OrderBean  extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String order_id;
	private String itemnum;
	private String amount;
	private String createtime;
	private String pay_status;
	private String ship_status;
	private String status;
//	private String share_money_tips;
	private String item;
	
	public OrderBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getItemnum() {
		return itemnum;
	}
	public void setItemnum(String itemnum) {
		this.itemnum = itemnum;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getShip_status() {
		return ship_status;
	}
	public void setShip_status(String ship_status) {
		this.ship_status = ship_status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
//	public String getShare_money_tips() {
//		return share_money_tips;
//	}
//	public void setShare_money_tips(String share_money_tips) {
//		this.share_money_tips = share_money_tips;
//	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	@Override
	public String toString() {
		return "OrderBean [order_id=" + order_id + ", itemnum=" + itemnum
				+ ", amount=" + amount + ", createtime=" + createtime
				+ ", pay_status=" + pay_status + ", ship_status=" + ship_status
				+ ", status=" + status + ", item=" + item + "]";
	}


	public OrderBean(String order_id, String itemnum, String amount,
			String createtime, String pay_status, String ship_status,
			String status, String item) {
		super();
		this.order_id = order_id;
		this.itemnum = itemnum;
		this.amount = amount;
		this.createtime = createtime;
		this.pay_status = pay_status;
		this.ship_status = ship_status;
		this.status = status;
		this.item = item;
	}
	
}
