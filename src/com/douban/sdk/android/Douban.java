package com.douban.sdk.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.douban.sdk.android.util.Utility;


/**
 * 
 * @author feng_xiang
 *
 */
public class Douban {
	public static String URL_OAUTH2_ACCESS_AUTHORIZE = "https://www.douban.com/service/auth2/auth";
	public static String app_key = "";//第三方应用的appkey
	public static String redirecturl = "";// 重定向url
	public static boolean isWifi = false;//当前是否为wifi
	public Oauth2AccessToken accessToken = null;//AccessToken实例
	public static final String KEY_TOKEN = "access_token";
	public static final String KEY_EXPIRES = "expires_in";
	public static final String KEY_REFRESHTOKEN = "refresh_token";
	public static final String DOUBAN_USERID = "douban_user_id";
	private static Douban mDoubanInstance = null;

	public synchronized static Douban getInstance(String appKey, String redirectUrl) {
		if (mDoubanInstance == null) {
			mDoubanInstance = new Douban();
		}
		app_key = appKey;
		Douban.redirecturl = redirectUrl;
		return mDoubanInstance;
	}
	/**
	 * 设定第三方使用者的appkey和重定向url
	 * @param appKey 第三方应用的appkey
	 * @param redirectUrl 第三方应用的回调页
	 */
	public void setupConsumerConfig(String appKey,String redirectUrl) {
		app_key = appKey;
		redirecturl = redirectUrl;
	}
	public void authorize(Context context, DoubanAuthListener listener) {
		isWifi = Utility.isWifi(context);
		startAuthDialog(context, listener);
	}
	
	public void startAuthDialog(Context context, final DoubanAuthListener listener) {
		MyParameters params = new MyParameters();
//		CookieSyncManager.createInstance(context);
		startDialog(context, params, new DoubanAuthListener() {
			@Override
			public void onComplete(Bundle values) {
				// ensure any cookies set by the dialog are saved
				CookieSyncManager.getInstance().sync();
				if (null == accessToken) {
					accessToken = new Oauth2AccessToken();
				}
				accessToken.setToken(values.getString(KEY_TOKEN));
				accessToken.setExpiresIn(values.getString(KEY_EXPIRES));
				accessToken.setRefreshToken(values.getString(KEY_REFRESHTOKEN));
				if (accessToken.isSessionValid()) {
					Log.d("Douban-authorize","Login Success! access_token=" + accessToken.getToken() + " expires="
									+ accessToken.getExpiresTime() + " refresh_token="
									+ accessToken.getRefreshToken());
					listener.onComplete(values);
				} else {
					Log.d("Douban-authorize", "Failed to receive access token");
					listener.onDoubanException(new DoubanException("Failed to receive access token."));
				}
			}

			@Override
			public void onError(DoubanDialogError error) {
				Log.d("Douban-authorize", "Login failed: " + error);
				listener.onError(error);
			}

			@Override
			public void onDoubanException(DoubanException error) {
				Log.d("Douban-authorize", "Login failed: " + error);
				listener.onDoubanException(error);
			}

			@Override
			public void onCancel() {
				Log.d("Douban-authorize", "Login canceled");
				listener.onCancel();
			}
		});
	}
	
	public void startDialog(Context context, MyParameters parameters,final DoubanAuthListener listener) {
		parameters.add("client_id", app_key);
		parameters.add("response_type", "code");
		parameters.add("redirect_uri", redirecturl);
		if (accessToken != null && accessToken.isSessionValid()) {
			parameters.add(KEY_TOKEN, accessToken.getToken());
		}
		String url = URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);
		if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			Utility.showAlert(context, "Error","Application requires permission to access the Internet");
		} else {
			new DoubanDialog(context, url, listener).show();
		}
	}


}
