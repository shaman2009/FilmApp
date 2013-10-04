package com.weibo.sdk.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.weibo.sdk.android.demo.Fragment.MenuFragment;
import com.weibo.sdk.android.demo.Fragment.PostFragment;
import com.weibo.sdk.android.demo.SocialNetworkRequest.SocialNetworkRequest;

public class SlidingmenuActivity extends BaseActivity {
	private Fragment mContent;
	/**
	 * @param titleRes
	 */
	public SlidingmenuActivity() {
		super(R.string.changing_fragments);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		
		if (mContent == null) {
			mContent = new PostFragment();	
		}
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuFragment())
		.commit();
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	
	
	public void onSubmitButtonClick(View v) {
		EditText editText = (EditText)findViewById(R.id.post_content);
		SocialNetworkRequest.sendMessage(this, editText.getText().toString());
	}
	public void onTagButtonClick(View v) {
		EditText editText = (EditText)findViewById(R.id.post_content);
		int i = editText.getSelectionStart();
		editText.append("##");
		editText.setSelection(i + 1);
	}
	public void onAtButtonClick(View v) {
		//TODO all intent put into util
		Intent intent = new Intent(this, FriendListActivity.class);
	    startActivity(intent);
	}

}
