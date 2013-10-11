package com.filmapp.message;

import com.weibo.sdk.android.demo.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * 
 * @author wen_xiang
 *
 * 2013-10-11
 */
public class MessageView extends FrameLayout{
	
	private static final int mLeftOff = 110 ; 
	
	public static final String ICON_TAG = "head_icon_view";
	public static final String TITLE_TAG = "title_view";
	public static final String CONTENT_TAG = "content_view";
	public static final String SCRIPT_TAG = "script_view";
	
	/**
	 * It is head icon image view
	 */
	ImageView mHeadIconView ;
	TextView mTitle ;
	View mContentView ;
	TextView mScriptText ;
	
	public MessageView(Context context) {
		super(context);
		addHeadIcon();
		addTitle();
		addContent();
		addScript();
	}
	
	public void addHeadIcon(){
		mHeadIconView = new ImageView(getContext());
		mHeadIconView.setScaleType(ScaleType.FIT_XY);
		mHeadIconView.setBackgroundResource(R.drawable.ic_launcher);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(100,100);
		lp.leftMargin = 10 ;
		lp.topMargin = 10 ;
		mHeadIconView.setLayoutParams(lp);
		mHeadIconView.setTag(mHeadIconView);
		addView(mHeadIconView);
	}
	
	public void addTitle(){
		mTitle = new TextView(getContext());
		mTitle.setTextSize(20);
		mTitle.setTextColor(0xff222222);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(580,80);
		lp.leftMargin = mLeftOff ;
		lp.topMargin = 10 ;
		mTitle.setLayoutParams(lp);
		mTitle.setTag(TITLE_TAG);
		addView(mTitle);
	}
	
	public void addContent(){
		mContentView = new View(getContext());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(580,250);
		lp.leftMargin = mLeftOff ;
		lp.topMargin = 110 ;
		mContentView.setBackgroundResource(R.drawable.title_logo);
		mContentView.setLayoutParams(lp);
		mContentView.setTag(CONTENT_TAG);
		addView(mContentView);
	}
	
	public void addScript(){
		mScriptText = new TextView(getContext());
		mScriptText.setTextSize(15);
		mScriptText.setTextColor(Color.GRAY);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(580,80);
		lp.leftMargin = mLeftOff ;
		lp.topMargin = 360 ;
		mScriptText.setLayoutParams(lp);
		mScriptText.setTag(SCRIPT_TAG);
		addView(mScriptText);
	}
	
	public static class ViewHolder{
		public ImageView mHeadIconView ;
		public TextView mTitle ;
		public View mContentView ;
		public TextView mScriptText ;
	}
	
}
