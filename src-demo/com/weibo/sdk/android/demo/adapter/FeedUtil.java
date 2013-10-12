package com.weibo.sdk.android.demo.adapter;


import java.util.Date;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.weibo.sdk.android.demo.R;
import com.weibo.sdk.android.demo.entity.Feed;
import com.weibo.sdk.android.demo.entity.User;
import com.weibo.sdk.android.util.StringUtil;
import com.weibo.sdk.android.util.TimeSpanUtil;


public class FeedUtil {
	public static View initConvertView(Context context, View convertView) {
		if (convertView != null
			&& convertView.getId() == R.id.llStatus) {
			return convertView;
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.list_item_status, null);
		FeedHolder holder = new FeedHolder(convertView);
		convertView.setTag(holder);
		return convertView;
	}
	

	public static View fillConvertView(View convertView, Feed feed) {
		java.util.Random r = new java.util.Random(); // 这个是专门产生随机数的类
		int x = 1;
		for (int i = 0; i < 10; i++) {
			x = r.nextInt(3) + 1;
		}
		Context context = convertView.getContext();
        FeedHolder holder = (FeedHolder)convertView.getTag();
        if (holder == null) {
        	return null;
        }
        holder.reset();

		holder.ivProfilePicture.setVisibility(View.VISIBLE);
	    
		holder.tvCreatedAt.setText(TimeSpanUtil.toTimeSpanString(new Date().getDate()));
		ImageView ivTempThumbnail = holder.ivThumbnail;
		ivTempThumbnail.setImageResource(R.drawable.com_facebook_logo);
		ivTempThumbnail.setVisibility(View.VISIBLE);
		String responseText = String.format("test", 1, 2);
		holder.tvResponse.setText(responseText);

		if(x == 1) {
			holder.ivProfilePicture.setImageResource(R.drawable.xx);
			holder.tvText.setText("It looks like the head of a dandelion seed, you know, with all the lollipops coming out of it, and with a center in it, that we started out as the tweet. ");
			holder.tvSource.setText("facebook");
			ivTempThumbnail.setImageResource(R.drawable.com_facebook_logo);
		} else if( x == 2) {
			holder.ivProfilePicture.setImageResource(R.drawable.shaman);
			holder.tvText.setText("The wind puffed away the seeds of the dandelion. ");
			holder.tvSource.setText("facebook");
			ivTempThumbnail.setImageResource(R.drawable.and);
		} else if( x == 3) {
			holder.ivProfilePicture.setImageResource(R.drawable.xxx);
			holder.tvText.setText("To satisfy the sweeter taste buds, check out this awesome recipe for Dandelion syrup. ");
			holder.tvSource.setText("twitter");
			ivTempThumbnail.setImageResource(R.drawable.x);
		} 



		return convertView;
	}

}
