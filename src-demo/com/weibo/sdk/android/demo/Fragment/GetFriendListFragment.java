package com.weibo.sdk.android.demo.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.demo.R;
import com.weibo.sdk.android.demo.SlidingmenuActivity;
import com.weibo.sdk.android.demo.SocialNetworkRequest.SocialNetworkRequest;
import com.weibo.sdk.android.demo.SocialNetworkRequest.WeiboUserInfoPO;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.util.FileManager;
import com.weibo.sdk.android.util.GetImage;

public class GetFriendListFragment extends Fragment {

	private ArrayList<Data> mDatas = new ArrayList<Data>();
	private BaseAdapter mAdapter;
	private int mHeight;
	private int mWidth;
	private int mSize = 8;
	public static List<WeiboUserInfoPO> list;
	private ArrayList<Data> selectMap = new ArrayList<Data>();  
	AsyncTask<String, Void, String> asyncTask;
	public static final String ADDFRIENDLISTNAME = "ADDFRIENDLISTNAME";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			// mDatas = (ArrayList<Data>)
			// savedInstanceState.getSerializable("listViewData");
		}
		if (asyncTask != null) {
			asyncTask.cancel(true);
		}
		
		// get friendlist info from weibo
		final AlertDialog builder = new AlertDialog.Builder(getActivity()).setMessage("Loading").show(); 
		
//		list = PostFragment.list;
		initData(list);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;

		FrameLayout mFrame = new FrameLayout(getActivity());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		mFrame.setLayoutParams(lp);

		ListView mList = new ListView(getActivity());
		mAdapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return mDatas.size();
			}
			@Override
			public Object getItem(int position) {
				return mDatas.get(position);
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				ViewHolder mHolder;
				if (convertView == null) {
					mHolder = new ViewHolder();
					ItemLayout item = new ItemLayout(getActivity());
					mHolder.mText = (TextView) item.findViewWithTag(ItemLayout.TEXT_TAG);
					mHolder.mImage = (ImageView) item.findViewWithTag(ItemLayout.IMAGE_TAG);
					mHolder.mCheck = (CheckBox) item.findViewWithTag(ItemLayout.CHECK_TAG);
					convertView = item;
					convertView.setTag(mHolder);
				} else {
					mHolder = (ViewHolder) convertView.getTag();
				}
				final Data d = mDatas.get(position);
				if (d == null) {
					return convertView;
				}
				mHolder.mText.setText(d.mName);
				mHolder.mImage.setImageBitmap(d.mImage);
				boolean checked =  selectMap.contains(d);
				Log.e("test", "eda" + checked);
				if(checked) {
					mHolder.mCheck.setChecked(true);
				} else {
					mHolder.mCheck.setChecked(false);
				}
				mHolder.mCheck.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(selectMap.contains(d)) {
							selectMap.remove(d);
							Log.d("qweeeeee", d.mName);
						}else {
								selectMap.add(d);
								Log.d("qwe", d.mName);
						}
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								mAdapter.notifyDataSetChanged();
							}
						});
					}
				});
				return convertView;
			}
		};
		mList.setAdapter(mAdapter);
		
		
		
		Button addButton = new Button(getActivity());
		addButton.setText("ADD");
		addButton.setTextSize(20);
		addButton.setGravity(Gravity.CENTER);
		
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int z = selectMap.size();
				if(z == 0) {
					Toast.makeText(getActivity(), "please check some of your friends", Toast.LENGTH_LONG).show();
					return;
				}
				String str = "";
				Log.d("qwe", "size : " + z);
				Iterator<Data> it = selectMap.iterator();
				while (it.hasNext()) {
					str = str + "@" + it.next().mName + " ";
				}
				Intent intent = new Intent(getActivity(), SlidingmenuActivity.class);
				intent.putExtra(ADDFRIENDLISTNAME, str);
			    startActivity(intent);
			    getActivity().finish();
			}
		});
		
		FrameLayout.LayoutParams mListLayoutParams = new FrameLayout.LayoutParams(mWidth, mHeight/ mSize* (mSize-1));
		mListLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mList.setLayoutParams(mListLayoutParams);
		FrameLayout.LayoutParams addButtonLayoutParams = new FrameLayout.LayoutParams(mWidth, mHeight/mSize);
		addButtonLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		addButton.setLayoutParams(addButtonLayoutParams);
		
		
		
		mFrame.addView(mList);
		mFrame.addView(addButton);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SocialNetworkRequest.getFriendList(getActivity(),
							new RequestListener() {
								@Override
								public void onIOException(IOException e) {
									Log.e("test", "onIOException" );
									new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getActivity(), "GETFRIENDLIST IOException!!!", Toast.LENGTH_LONG).show();
										}
									};
								}
								@Override
								public void onError(WeiboException e) {
									Log.e("test", "on error");
									getActivity().runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getActivity(), "GETFRIENDLIST Error!!!", Toast.LENGTH_LONG).show();
										}
									});
								}
								@Override
								public void onComplete(final String response) {
									Log.e("test", "on complete");
									SharedPreferences pref = getActivity().getSharedPreferences(PostFragment.FRIENDLIST, Context.MODE_APPEND);
									Editor editor = pref.edit();
									editor.putString(PostFragment.FRIENDLIST, response);
									editor.commit();
									asyncTask.execute(response);
								}
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
		asyncTask = new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				Log.e("test", "do in back");
				list = new ArrayList<WeiboUserInfoPO>();
				JSONObject json;
				try {
					json = new JSONObject(params[0]);
					JSONArray array = json.getJSONArray("users");
					int count = 0;
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonWeiboUser = array.getJSONObject(i);
						WeiboUserInfoPO weiboUserInfoPO = new WeiboUserInfoPO();
						weiboUserInfoPO.setId(jsonWeiboUser.getString("id"));
						weiboUserInfoPO.setAvatar_large(jsonWeiboUser.getString("avatar_large"));
						weiboUserInfoPO.setName(jsonWeiboUser.getString("name"));
						weiboUserInfoPO.setScreen_name(jsonWeiboUser.getString("screen_name"));
						weiboUserInfoPO.setPic_path(getActivity().getFilesDir() + "/filmApp/" + weiboUserInfoPO.getId() + ".jpg");
						count++;
						list.add(weiboUserInfoPO);
						if (count > 10) {
							Log.i(PostFragment.TAG, list + " " + count);
							count = 0;
							initData(list);
							publishProgress();
						}
						File file = new File(weiboUserInfoPO.getPic_path());
						if(!file.exists()) {
//						if(true) {
							Bitmap bitmap = GetImage.getHttpBitmap(weiboUserInfoPO.getAvatar_large());
							FileManager.saveBitmapToFile(bitmap, weiboUserInfoPO.getPic_path());
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return "";
			}
			@Override
			protected void onProgressUpdate(Void... values) {
				Log.e("test", "update progress");
				super.onProgressUpdate(values);
				if (mAdapter != null) {
					builder.cancel();
					Log.i(PostFragment.TAG, "onProgressUpdate");
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mAdapter.notifyDataSetChanged();
						}
					});
				}
			}

			@Override
			protected void onPostExecute(String result) {
				Log.e("test", "post execute");
				super.onPostExecute(result);
			}
		};
		return mFrame;
	}

	class Data {
		Bitmap mImage;
		String mName;
		@Override
		public boolean equals(Object o) {
			if (o instanceof Data) {
				return mImage == ((Data) o).mImage && String.valueOf(mName).equals(((Data) o).mName);
			}
			return false;
		}
	}

	class ViewHolder {
		TextView mText;
		ImageView mImage;
		CheckBox mCheck;
	}

	class ItemLayout extends FrameLayout {

		public static final String TEXT_TAG = "mcontext";
		public static final String IMAGE_TAG = "image";
		public static final String CHECK_TAG = "check";
		ImageView mImage;
		TextView mContent;
		CheckBox mCheck;

		public ItemLayout(Context context) {
			super(context);
			init();
		}

		public void init() {
			addText(this);
			addImage();
			addCheck();
		}

		public void addImage() {
			mImage = new ImageView(getContext());
			LayoutParams lp = new LayoutParams(100, 100);
			lp.gravity = Gravity.LEFT | Gravity.TOP;
			mImage.setTag(IMAGE_TAG);
			mImage.setBackgroundResource(R.drawable.ic_launcher);
			addView(mImage, lp);
		}

		public void addCheck() {
			mCheck = new CheckBox(getActivity());
			LayoutParams lp = new LayoutParams(100, 100);
			lp.gravity = Gravity.RIGHT | Gravity.TOP;
			mCheck.setTag(CHECK_TAG);
			addView(mCheck, lp);
		}

		public void addText(FrameLayout mF) {
			mContent = new TextView(getContext());
			mContent.setTag(TEXT_TAG);
			mContent.setGravity(Gravity.CENTER);
			mContent.setTextSize(20);
			mContent.setText("hello");
			LayoutParams lp = new LayoutParams(mWidth - 100, 100);
			lp.gravity = Gravity.LEFT | Gravity.TOP;
			lp.leftMargin = 110;
			addView(mContent, lp);
		}

	}
	
	public void initData(List<WeiboUserInfoPO> list) {
		if (list == null) {
			return;
		}
		for (WeiboUserInfoPO weiboUserInfoPO : list) {
			Data data = new Data();
			data.mImage = FileManager.loadBitmapFromFile(weiboUserInfoPO.getPic_path());
			data.mName = weiboUserInfoPO.getName();
			mDatas.add(data);
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public void onDestroyView() {
		if (asyncTask != null) {
			asyncTask.cancel(true);
		}
		super.onDestroyView();
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("listViewData", mDatas);
	}
}
