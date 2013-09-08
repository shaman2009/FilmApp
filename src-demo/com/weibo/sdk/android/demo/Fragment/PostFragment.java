/**
 * Sep 8, 2013
 * PostFragment.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 4:09:12 PM
 * Sep 8, 2013
 * PostFragment.java
 * @author fengxiang
 */
public class PostFragment extends Fragment{
	
	private int mHeight;
	private int mWidth;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			
			
		}
		
		
		DisplayMetrics  dm = new DisplayMetrics();    
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);    
	    mWidth = dm.widthPixels;              
		mHeight = dm.heightPixels;  
		
		
		
		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mWidth,mHeight *4/5);
		
		
		
		EditText editText = new EditText(getActivity());
		editText.setHint("Thinking in Java");
		editText.setTextSize(20);
		editText.setFocusable(true);   
		editText.setFocusableInTouchMode(true);   
		editText.requestFocus();  
		editText.setLayoutParams(lp);
		editText.setId(999999);
		editText.setGravity(Gravity.LEFT | Gravity.TOP);
		
		//Group buttons
		FrameLayout frameLayout = new FrameLayout(getActivity());
		Button button_at,button_emoji,button_pic,button_tag,button_location;
		button_at = new Button(getActivity());
		button_emoji = new Button(getActivity());
		button_pic = new Button(getActivity());
		button_tag = new Button(getActivity());
		button_location = new Button(getActivity());
		
		button_at.setText("@");
		button_emoji.setText("E");
		button_pic.setText("P");
		button_tag.setText("#");
		button_location.setText("L");
		
		
		FrameLayout.LayoutParams layoutParamsat = new FrameLayout.LayoutParams(100,50);
		layoutParamsat.gravity = Gravity.RIGHT | Gravity.TOP;
		FrameLayout.LayoutParams layoutParamsemoji = new FrameLayout.LayoutParams(100,50);
		layoutParamsemoji.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamsemoji.rightMargin = 120;
		FrameLayout.LayoutParams layoutParamspic = new FrameLayout.LayoutParams(100,50);
		layoutParamspic.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamspic.rightMargin =240;
		FrameLayout.LayoutParams layoutParamstag = new FrameLayout.LayoutParams(100,50);
		layoutParamstag.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamstag.rightMargin = 360;
		FrameLayout.LayoutParams layoutParamslocation = new FrameLayout.LayoutParams(100,50);
		layoutParamslocation.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamslocation.rightMargin = 480;
		
		button_at.setLayoutParams(layoutParamsat);
		button_emoji.setLayoutParams(layoutParamsemoji);
		button_pic.setLayoutParams(layoutParamspic);
		button_tag.setLayoutParams(layoutParamstag);
		button_location.setLayoutParams(layoutParamslocation);
		
		frameLayout.addView(button_at);
		frameLayout.addView(button_emoji);
		frameLayout.addView(button_pic);
		frameLayout.addView(button_tag);
		frameLayout.addView(button_location);
		
		
		
		RelativeLayout.LayoutParams frameLayoutParams = new RelativeLayout.LayoutParams(mWidth,60);
		frameLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, 999999);
		frameLayout.setLayoutParams(frameLayoutParams);
		
		v.addView(editText);
		v.addView(frameLayout);
		
		
		
		
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//todo
	}
	
}
