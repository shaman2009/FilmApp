package com.weibo.sdk.android.demo.entity;

import java.util.Date;


public class Feed {
	/** 微博信息ID */
	private String statusId;
	/** 微博信息内容 */
	private String text;
	/** 微博信息来源 */
	private String source;
	/** 是否已收藏 */
	private boolean isFavorited;
	/** 是否被截断 */
	private boolean isTruncated;

	/** 被回复微博消息ID */
	private String inReplyToStatusId;

	/** 转发的微博信息 */
	private Feed retweetedStatus;
	/** 作者信息 */
	private User user = null;

	/** 原图 */
	private String originalPictureUrl;
	/** 中型图片 */
	private String middlePictureUrl;
	/** 缩略图 */
	private String thumbnailPictureUrl;

	private Location location = null;

	/** 转发数 */
	private Integer retweetCount;
	/** 评论数 */
	private Integer commentCount;

	/** 创建时间 */
	private Date createdAt;

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public boolean isTruncated() {
		return isTruncated;
	}

	public void setTruncated(boolean isTruncated) {
		this.isTruncated = isTruncated;
	}

	public String getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(String inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public Feed getRetweetedStatus() {
		return retweetedStatus;
	}

	public void setRetweetedStatus(Feed retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOriginalPictureUrl() {
		return originalPictureUrl;
	}

	public void setOriginalPictureUrl(String originalPictureUrl) {
		this.originalPictureUrl = originalPictureUrl;
	}

	public String getMiddlePictureUrl() {
		return middlePictureUrl;
	}

	public void setMiddlePictureUrl(String middlePictureUrl) {
		this.middlePictureUrl = middlePictureUrl;
	}

	public String getThumbnailPictureUrl() {
		return thumbnailPictureUrl;
	}

	public void setThumbnailPictureUrl(String thumbnailPictureUrl) {
		this.thumbnailPictureUrl = thumbnailPictureUrl;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(Integer retweetCount) {
		this.retweetCount = retweetCount;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
