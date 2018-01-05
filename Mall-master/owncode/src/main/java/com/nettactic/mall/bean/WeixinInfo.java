package com.nettactic.mall.bean;

public class WeixinInfo {

	private String sex, nickname, unionid, province, openid, language,
			headimgurl, country, city;

	public WeixinInfo() {
		super();
	}

	public WeixinInfo(String sex, String nickname, String unionid,
			String province, String openid, String language, String headimgurl,
			String country, String city) {
		super();
		this.sex = sex;
		this.nickname = nickname;
		this.unionid = unionid;
		this.province = province;
		this.openid = openid;
		this.language = language;
		this.headimgurl = headimgurl;
		this.country = country;
		this.city = city;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "WeixinInfo [sex=" + sex + ", nickname=" + nickname
				+ ", unionid=" + unionid + ", province=" + province
				+ ", openid=" + openid + ", language=" + language
				+ ", headimgurl=" + headimgurl + ", country=" + country
				+ ", city=" + city + "]";
	}

}
