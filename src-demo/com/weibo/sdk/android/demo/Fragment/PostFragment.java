/**
 * Sep 8, 2013
 * PostFragment.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.Fragment;

import com.weibo.sdk.android.demo.FriendListActivity;
import com.weibo.sdk.android.demo.MainActivity;
import com.weibo.sdk.android.demo.MovieActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	public static final int INPUTEDITTEXTID = 999999;
	public static final int BUTTONSGROUP = 999998;
	
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
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mWidth,mHeight *2/5);
		
		
		
		EditText editText = new EditText(getActivity());
		editText.setHint("Thinking in Java");
		editText.setTextSize(20);
		editText.setFocusable(true);   
		editText.setFocusableInTouchMode(true);   
		editText.requestFocus();  
		editText.setLayoutParams(lp);
		editText.setId(INPUTEDITTEXTID);
		editText.setGravity(Gravity.LEFT | Gravity.TOP);
		
		//Group buttons
		FrameLayout frameLayout = new FrameLayout(getActivity());
		frameLayout.setId(BUTTONSGROUP);
		Button button_at,button_emoji,button_pic,button_tag,button_location;
		button_at = new Button(getActivity());
		button_emoji = new Button(getActivity());
		button_pic = new Button(getActivity());
		button_tag = new Button(getActivity());
		button_location = new Button(getActivity());
		Button button_post = new Button(getActivity());
		
		button_post.setText("POST");
		button_at.setText("@");
		button_emoji.setText("E");
		button_pic.setText("P");
		button_tag.setText("#");
		button_location.setText("L");
		
		button_at.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						getFriendListActivity("");
						Looper.loop();
					}
				}).start();
			}
		});
		
		
		int button_width = 100;
		int button_height = 63;
		
		FrameLayout.LayoutParams layoutParamspost = new FrameLayout.LayoutParams(220,button_height);
		layoutParamspost.gravity = Gravity.RIGHT | Gravity.TOP;
		FrameLayout.LayoutParams layoutParamsat = new FrameLayout.LayoutParams(button_width,button_height);
		layoutParamsat.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamsat.rightMargin = 230;
		FrameLayout.LayoutParams layoutParamstag = new FrameLayout.LayoutParams(button_width,button_height);
		layoutParamstag.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamstag.rightMargin = 350;
		FrameLayout.LayoutParams layoutParamsemoji = new FrameLayout.LayoutParams(button_width,button_height);
		layoutParamsemoji.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamsemoji.rightMargin = 350;
		FrameLayout.LayoutParams layoutParamspic = new FrameLayout.LayoutParams(button_width,button_height);
		layoutParamspic.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamspic.rightMargin =240;
		FrameLayout.LayoutParams layoutParamslocation = new FrameLayout.LayoutParams(button_width,button_height);
		layoutParamslocation.gravity = Gravity.RIGHT | Gravity.TOP;
		layoutParamslocation.rightMargin = 480;
		
		button_post.setLayoutParams(layoutParamspost);
		button_at.setLayoutParams(layoutParamsat);
		button_emoji.setLayoutParams(layoutParamsemoji);
		button_pic.setLayoutParams(layoutParamspic);
		button_tag.setLayoutParams(layoutParamstag);
		button_location.setLayoutParams(layoutParamslocation);
		
		frameLayout.addView(button_post);
		frameLayout.addView(button_at);
		frameLayout.addView(button_tag);
//		frameLayout.addView(button_emoji);
//		frameLayout.addView(button_pic);
//		frameLayout.addView(button_location);
		
		FrameLayout postButtonFrameLayout = new FrameLayout(getActivity());
		

		
		
		
		
		
		RelativeLayout.LayoutParams frameLayoutParams = new RelativeLayout.LayoutParams(mWidth,60);
		frameLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, INPUTEDITTEXTID);
		frameLayout.setLayoutParams(frameLayoutParams);
		
		
		
		v.addView(editText);
		v.addView(frameLayout);
		v.addView(postButtonFrameLayout);
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//todo
	}
	
	
	
	public void getFriendListActivity(String s) {
		Intent intent = new Intent(getActivity(), FriendListActivity.class);
//	    intent.putExtra(MOVIE_MESSAGE, s);
	    startActivity(intent);
	}
}
