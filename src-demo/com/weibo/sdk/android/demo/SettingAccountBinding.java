package com.weibo.sdk.android.demo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.sso.SsoHandler;

public class SettingAccountBinding extends Activity {

	private Button weiboButton;
	private Button doubanButton;
	private Button facebookButton;
	
	private Weibo mWeibo;
	private SsoHandler mSsoHandler;
	public static Oauth2AccessToken accessToken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_account_binding);
		// Show the Up button in the action bar.
		setupActionBar();
		mWeibo = Weibo.getInstance(LoginActivity.WEIBO_CONSUMER_KEY, LoginActivity.WEIBO_REDIRECT_URL);
		
		weiboButton = (Button)this.findViewById(R.id.setting_account_binding_weibo_yes);
		
		if(LoginActivity.readloginsession(this)) {
			weiboButton.setText(R.string.setting_account_binding_yes);
			weiboButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LoginActivity.deleteloginsession(v.getContext());
					weiboButton.setText(R.string.setting_account_binding_no);
					Toast.makeText(SettingAccountBinding.this, "unbind success", Toast.LENGTH_SHORT).show();
					
				}
			});
		}else {
			weiboButton.setText(R.string.setting_account_binding_no);
			weiboButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mSsoHandler = new SsoHandler(SettingAccountBinding.this, mWeibo);
					mSsoHandler.authorize(new WeiboAuthDialogListener());
				}
			});
		}
		
		
		
		facebookButton = (Button)this.findViewById(R.id.setting_account_binding_facebook_yes);
		facebookButton.setText(R.string.setting_account_binding_no);
		doubanButton = (Button)this.findViewById(R.id.setting_account_binding_douban_yes);
		doubanButton.setText(R.string.setting_account_binding_no);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting_account_binding, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	class WeiboAuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			if (accessToken.isSessionValid()) {
				try {
					Class sso = Class.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
				} catch (ClassNotFoundException e) {
				}
				AccessTokenKeeper.keepAccessToken(SettingAccountBinding.this,LoginActivity.accessToken);
				Toast.makeText(SettingAccountBinding.this, "bind success", Toast.LENGTH_SHORT).show();
				weiboButton.setText(R.string.setting_account_binding_yes);
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
