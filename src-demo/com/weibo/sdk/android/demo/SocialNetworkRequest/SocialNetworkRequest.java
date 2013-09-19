/**
 * Sep 9, 2013
 * SocialNetworkRequest.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.SocialNetworkRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.douban.sdk.android.Douban;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.demo.MainActivity;
import com.weibo.sdk.android.demo.MovieActivity;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.util.FileManager;
import com.weibo.sdk.android.util.GetImage;

/**
 * 9:33:11 PM Sep 9, 2013 SocialNetworkRequest.java
 * 
 * @author fengxiang
 */
public class SocialNetworkRequest {

	private static Weibo mWeibo;
	private static Douban mDouban;
	private static final String WEIBO_CONSUMER_KEY = "2867503323";// 替换为开发者的appkey，例如"1646212860";
	private static final String WEIBO_REDIRECT_URL = "http://jianshu.io/users/SLfZEb";

	private static final String DOUBAN_CONSUMER_KEY = "0e32be84b39035752a983b8e1ab0a05f";
	private static final String DOUBAN_CLIENT_SECRET = "a560769d3d9b0dc7";
	private static final String DOUBAN_REDIRECT_URL = "http://shaman.logdown.com";

	private Button authBtn, apiBtn, ssoBtn, cancelBtn, doubanAuthBtn;
	// private Text mText;
	private EditText mEditText;
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";
	public static final String USERID = "userId";
	public static final String FRIENDLIST = "friendList";

	public final static String MOVIE_MESSAGE = "douban.movie.json";

	public static void getUserId(final Context context, RequestListener requestListener) throws JSONException {
		mWeibo = Weibo.getInstance(WEIBO_CONSUMER_KEY, WEIBO_REDIRECT_URL);
		accessToken = AccessTokenKeeper.readAccessToken(context);
		StatusesAPI statusesAPI = new StatusesAPI(accessToken);
		statusesAPI.userTimeline(0L, 0L, 1, 1, false, FEATURE.ALL, false, requestListener);
	}

	public static void getFriendList(final Context context, RequestListener requestListener)
			throws JSONException {
		FriendshipsAPI friendshipsAPI = new FriendshipsAPI(accessToken);
		SharedPreferences pref = context.getSharedPreferences(USERID,
				Context.MODE_APPEND);
		String userId = pref.getString("userId", "");
		if (userId == null || "".equals(userId)) {
			((Activity) context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {

					Toast.makeText(context, "You have not got userId yet!!!",
							Toast.LENGTH_LONG).show();
				}
			});
			return;
		}
		long uid = Long.valueOf(userId);
		friendshipsAPI.friends(uid, 50, 0, false, requestListener);
	}
}
