/**
 * Sep 9, 2013
 * WeiboUserInfoPO.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.SocialNetworkRequest;

/**
 * 10:23:22 PM Sep 9, 2013 WeiboUserInfoPO.java
 * 
 * @author fengxiang
 */
public class WeiboUserInfoPO {
	private String id;
	private String screen_name;
	private String name;
	private String avatar_large;
	private String pic_path;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

}
