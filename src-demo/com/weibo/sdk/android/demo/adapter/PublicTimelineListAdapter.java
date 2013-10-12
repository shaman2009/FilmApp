package com.weibo.sdk.android.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.weibo.sdk.android.demo.entity.Feed;

public class PublicTimelineListAdapter extends ArrayAdapter<Feed>{
	private Context context;
	public PublicTimelineListAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = FeedUtil.initConvertView(context, convertView);
		FeedUtil.fillConvertView(convertView, null);
		
		return convertView;
	}
	@Override
	public int getCount() {
		return 20;
	}
	
}
