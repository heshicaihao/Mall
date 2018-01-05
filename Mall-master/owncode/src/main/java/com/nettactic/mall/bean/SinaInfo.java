package com.nettactic.mall.bean;

public class SinaInfo {

	private String uid, favourites_count, location, description, verified,
			friends_count, gender, screen_name, statuses_count,
			followers_count, profile_image_url, access_token;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(String favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(String friends_count) {
		this.friends_count = friends_count;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}

	public String getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public String toString() {
		return "SinaInfo [uid=" + uid + ", favourites_count="
				+ favourites_count + ", location=" + location
				+ ", description=" + description + ", verified=" + verified
				+ ", friends_count=" + friends_count + ", gender=" + gender
				+ ", screen_name=" + screen_name + ", statuses_count="
				+ statuses_count + ", followers_count=" + followers_count
				+ ", profile_image_url=" + profile_image_url
				+ ", access_token=" + access_token + "]";
	}

	public SinaInfo(String uid, String favourites_count, String location,
			String description, String verified, String friends_count,
			String gender, String screen_name, String statuses_count,
			String followers_count, String profile_image_url,
			String access_token) {
		super();
		this.uid = uid;
		this.favourites_count = favourites_count;
		this.location = location;
		this.description = description;
		this.verified = verified;
		this.friends_count = friends_count;
		this.gender = gender;
		this.screen_name = screen_name;
		this.statuses_count = statuses_count;
		this.followers_count = followers_count;
		this.profile_image_url = profile_image_url;
		this.access_token = access_token;
	}

	public SinaInfo() {
		super();
	}

}
