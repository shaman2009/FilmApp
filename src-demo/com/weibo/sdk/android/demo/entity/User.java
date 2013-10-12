package com.weibo.sdk.android.demo.entity;
import java.util.Date;

public class User {
	private static final long serialVersionUID = 6924492479904061650L;
	
	/** 用户ID */
	protected String userId;
	/** 用户名 */
	protected String name;
	/** 显示名称 */
	protected String screenName;
	/** 头像图片地址 */
	protected String profileImageUrl;
	/** 性别 */
	protected String gender;
	
	/** 好友数或关注数 */
	private int friendsCount;
	/** 被关注数量或粉丝数 */
	private int followersCount;
	/** 签名或微博数 */
	private int statusesCount;
	
	/** 简单描述 */
	protected String description;
	/** 当前所在地 */
	protected String location;
	/** 是否经过认证 */
	protected boolean isVerified;

	protected Date createdAt;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
