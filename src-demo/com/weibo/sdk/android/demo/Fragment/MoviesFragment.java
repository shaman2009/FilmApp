/**
 * Sep 23, 2013
 * MoviesFranment.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.Fragment;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.douban.sdk.android.DoubanException;
import com.douban.sdk.android.net.DoubanRequestListener;
import com.weibo.sdk.android.demo.MovieActivity;
import com.weibo.sdk.android.demo.R;
import com.weibo.sdk.android.demo.SocialNetworkRequest.SocialNetworkRequest;

/**
 * 6:52:49 AM Sep 23, 2013 MoviesFranment.java
 * 
 * @author fengxiang
 */
public class MoviesFragment extends Fragment {
	private Button mBtnSearch;
	private EditText mEditText;
	
	private static final String TAG = "MoviesFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.search, container, false);
		mBtnSearch = (Button)v.findViewById(R.id.btnSearch);
		mEditText = (EditText)v.findViewById(R.id.etKeyWord);
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = mEditText.getEditableText().toString();
				SocialNetworkRequest.getDoubanMovie(getActivity(), str, new DoubanRequestListener() {
					
					@Override
					public void onIOException(IOException e) {
						Log.i(TAG, e.toString());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getActivity(), "getDoubanMovie IOException!!!",Toast.LENGTH_LONG).show();
							}
						});
					}
					
					@Override
					public void onError(DoubanException e) {
						Log.i(TAG, e.toString());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getActivity(), "getDoubanMovie Error!!!",Toast.LENGTH_LONG).show();
							}
						});
					}
					
					@Override
					public void onComplete(String response) {
						Log.i(TAG, response);
						try {
							JSONObject result = new JSONObject(response);
							final String s = result.toString();
							JSONObject movieResult;
							int i = 0;
							try {
								movieResult = new JSONObject(s);
								i = Integer.valueOf(movieResult.getString("total"));;
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if(i < 5) {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getActivity(), "No result!",Toast.LENGTH_LONG).show();
									}
								});
								return;
							}
							new Thread(new Runnable() {
								@Override
								public void run() {
									Looper.prepare();
									Intent intent = new Intent(getActivity(), MovieActivity.class);
								    intent.putExtra(MovieActivity.MOVIE_MESSAGE, s);
								    startActivity(intent);
									Looper.loop();
								}
							}).start();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		return v;
	}
}
