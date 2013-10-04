/**
 * Sep 8, 2013
 * PostFragment.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.Fragment;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.demo.R;
import com.weibo.sdk.android.demo.SocialNetworkRequest.SocialNetworkRequest;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 4:09:12 PM
 * Sep 8, 2013
 * PostFragment.java
 * @author fengxiang
 */
public class PostFragment extends Fragment {
	
	private EditText editText;
	private String addFriendListName;
	public static final int INPUTEDITTEXTID = 999999;
	public static final int BUTTONSGROUP = 999998;
	public static final String TAG = "sinasdk";
	public static final String USERID = "userId";
	public static final String POST_CONTENT = "post_content";
	public static final String FRIENDLIST = "friendList";
	
//	public static List<WeiboUserInfoPO> list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new SocialNetworkRequest(getActivity());
		Intent intent = getActivity().getIntent();
	    addFriendListName = intent.getStringExtra(GetFriendListFragment.ADDFRIENDLISTNAME);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
		}
		View v = inflater.inflate(R.layout.fragment_post, container, false);
		
		try {
			SharedPreferences prefUserId = getActivity().getSharedPreferences(USERID, Context.MODE_APPEND);
			String userId = prefUserId.getString("userId", "");
			if (userId == null || "".equals(userId)) {
				SocialNetworkRequest.getUserId(getActivity(),new RequestListener() {
					@Override
					public void onIOException(IOException e) {
						Log.i(TAG, e.toString());
						new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getActivity(),
										"userTimeline IOException!!!",
										Toast.LENGTH_LONG).show();
							}
						};
					}
	
					@Override
					public void onError(WeiboException e) {
						Log.i(TAG, e.toString());
						new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getActivity(),
										"userTimeline Error!!!",
										Toast.LENGTH_LONG).show();
							}
						};
					}
	
					@Override
					public void onComplete(String response) {
						Log.i(TAG, response);
						try {
							JSONObject json = new JSONObject(response);
							JSONArray array = json.getJSONArray("statuses");
							JSONObject firstFeed = array.getJSONObject(0);
							JSONObject user = firstFeed.getJSONObject("user");
							String userId = user.getString("id");
							Log.i("userTimeline", firstFeed.toString());
							Log.i("userTimeline", user.toString());
							Log.i("userTimeline", userId);
							SharedPreferences pref = getActivity().getSharedPreferences(USERID,Context.MODE_APPEND);
							Editor editor = pref.edit();
							editor.putString("userId", userId);
							editor.commit();
	
						} catch (JSONException e) {
							e.printStackTrace();
						}
	
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// 使得软键盘顶起界面 并且启动activity时候自动唤出
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		in.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
		return v;
	}
	@Override
	public void onStart() {
		super.onStart();
		editText = (EditText)getActivity().findViewById(R.id.post_content);
		SharedPreferences prefPostContent = getActivity().getSharedPreferences(POST_CONTENT, Context.MODE_APPEND);
		String postContent = prefPostContent.getString(POST_CONTENT, "");
		if(!"".equals(postContent)) {
			editText.setText(postContent);
			Editor editor = prefPostContent.edit();
			editor.putString(POST_CONTENT, "");
			editor.commit();
		}
		
		if(addFriendListName != null) {
			editText.append(addFriendListName);
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		SharedPreferences pref = getActivity().getSharedPreferences(POST_CONTENT,Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(POST_CONTENT, editText.getText().toString());
		editor.commit();
	}
	
}
