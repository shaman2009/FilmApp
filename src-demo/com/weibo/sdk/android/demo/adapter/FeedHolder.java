package com.weibo.sdk.android.demo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weibo.sdk.android.demo.R;


public class FeedHolder {
	private Context context;
	ImageView ivProfilePicture;
	TextView tvScreenName;
	TextView tvCreatedAt;
	TextView tvText;
	TextView tvRetweetText;
    View llRetweet;
    ImageView ivThumbnail;
    TextView tvImageInfo;
    TextView tvRetweetImageInfo;
    TextView tvResponse;
    TextView tvSource;
    
    public FeedHolder(View convertView) {
    	if (convertView == null) {
    		throw new IllegalArgumentException("convertView is null!");
    	}
    	context = convertView.getContext();
    	ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
    	tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
		tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
		tvText = (TextView) convertView.findViewById(R.id.tvText);
		ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
		tvImageInfo = (TextView) convertView.findViewById(R.id.tvImageInfo);
        tvResponse = (TextView) convertView.findViewById(R.id.tvResponse);
        tvSource = (TextView) convertView.findViewById(R.id.tvSource);

        reset();
    }

    /* 重新初始化 */
    public void reset() {
    	if (ivProfilePicture != null) {
    		ivProfilePicture.setVisibility(View.GONE);
    	}
    	if (tvScreenName != null) {
    		tvScreenName.setText("");
    	}
    	if (tvCreatedAt != null) {
    		tvCreatedAt.setText("");
    	}
    	if (tvText != null) {
    		tvText.setText("");
    	}
    	if (llRetweet != null) {
    		llRetweet.setVisibility(View.GONE);
    	}
    	if (tvRetweetText != null) {
    		tvRetweetText.setVisibility(View.GONE);
    		tvRetweetText.setText("");
    	}
    	if (ivThumbnail != null) {
    		ivThumbnail.setVisibility(View.GONE);
    	}
        
    	if (tvImageInfo != null) {
    		tvImageInfo.setVisibility(View.GONE);
    		tvImageInfo.setText("");
    	}
    	if (tvRetweetImageInfo != null) {
    		tvRetweetImageInfo.setVisibility(View.GONE);
    		tvRetweetImageInfo.setText("");
    	}

    }
}
