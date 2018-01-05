package com.nettactic.mall.bean;

import com.nettactic.mall.base.BaseBean;

import java.util.Comparator;

public class SearchHistoryInfoBean extends BaseBean  implements
		Comparator<SearchHistoryInfoBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String keyid;
	private String key;
	private String userid;
	private String lasttime;

	public SearchHistoryInfoBean() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyid() {
		return keyid;
	}

	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public SearchHistoryInfoBean(String id, String keyid, String key, String userid, String lasttime) {
		this.id = id;
		this.keyid = keyid;
		this.key = key;
		this.userid = userid;
		this.lasttime = lasttime;
	}

	public SearchHistoryInfoBean( String keyid, String key, String userid, String lasttime) {
		this.keyid = keyid;
		this.key = key;
		this.userid = userid;
		this.lasttime = lasttime;
	}

	@Override
	public String toString() {
		return "SearchHistoryInfoBean{" +
				"id='" + id + '\'' +
				", keyid='" + keyid + '\'' +
				", key='" + key + '\'' +
				", userid='" + userid + '\'' +
				", lasttime='" + lasttime + '\'' +
				'}';
	}

	@Override
	public int compare(SearchHistoryInfoBean bean0, SearchHistoryInfoBean bean1) {
		int num = 0;
		String timeStr0 = bean0.getLasttime();
		long time0 = Long.parseLong(timeStr0);

		String timeStr1 = bean1.getLasttime();
		long time1 = Long.parseLong(timeStr0);
		long value = time0 - time1;
		if (value > 0) {
			num = -1;
		} else if (value == 0) {
			num = 0;
		} else if (value < 0) {
			num = 1;
		}
		return num;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SearchHistoryInfoBean that = (SearchHistoryInfoBean) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (keyid != null ? !keyid.equals(that.keyid) : that.keyid != null) return false;
		if (key != null ? !key.equals(that.key) : that.key != null) return false;
		if (userid != null ? !userid.equals(that.userid) : that.userid != null) return false;
		return lasttime != null ? lasttime.equals(that.lasttime) : that.lasttime == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (keyid != null ? keyid.hashCode() : 0);
		result = 31 * result + (key != null ? key.hashCode() : 0);
		result = 31 * result + (userid != null ? userid.hashCode() : 0);
		result = 31 * result + (lasttime != null ? lasttime.hashCode() : 0);
		return result;
	}


}
