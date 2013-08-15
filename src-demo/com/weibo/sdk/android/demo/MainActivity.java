package com.weibo.sdk.android.demo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.douban.sdk.android.Douban;
import com.douban.sdk.android.DoubanAuthListener;
import com.douban.sdk.android.DoubanDialogError;
import com.douban.sdk.android.DoubanException;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.doubanapi.MovieAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.Utility;


/**
 * 
 * @author feng_xiang
 *
 */
public class MainActivity extends Activity {

	private Weibo mWeibo;
	private Douban mDouban;
	private static final String WEIBO_CONSUMER_KEY = "2867503323";// 替换为开发者的appkey，例如"1646212860";
	private static final String WEIBO_REDIRECT_URL = "http://jianshu.io/users/SLfZEb";
	
	private static final String DOUBAN_CONSUMER_KEY = "0e32be84b39035752a983b8e1ab0a05f";
	private static final String DOUBAN_REDIRECT_URL = "http://shaman.logdown.com";
	
	private Button authBtn, apiBtn, ssoBtn, cancelBtn ,doubanAuthBtn;
	private TextView mText;
	private EditText mEditText;
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";
	public static final String USERID = "userId";
	public static final String FRIENDLIST = "friendList";
	
	public final static String MOVIE_MESSAGE = "douban.movie.json";
	
	private AutoCompleteTextView mAutoText;  
	private String[] items = {"LovinEscaper","lorem", "ipsum", "dolor", "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat", "placerat", "ante", "porttitor", "sodales", "pellentesque", "augue", "purus"};   
	
	/**
	 * SsoHandler 仅当sdk支持sso时有效，
	 */
	SsoHandler mSsoHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWeibo = Weibo.getInstance(WEIBO_CONSUMER_KEY, WEIBO_REDIRECT_URL);
		mDouban = Douban.getInstance(DOUBAN_CONSUMER_KEY, DOUBAN_REDIRECT_URL);
//		mAutoText = (AutoCompleteTextView)findViewById(R.id .editAuto);  
//		mAutoText.setAdapter(new ArrayAdapter<String>(this ,android.R.layout.simple_dropdown_item_1line ,items ));  
		doubanAuthBtn = (Button) findViewById(R.id.doubanAuth);
		doubanAuthBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDouban.authorize(MainActivity.this, new DoubanAuthDialogListener());
			}
		});
		authBtn = (Button) findViewById(R.id.auth);
		authBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWeibo.authorize(MainActivity.this, new WeiboAuthDialogListener());
			}
		});
		ssoBtn = (Button) findViewById(R.id.sso);// 触发sso的按钮
		try {
			Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
			ssoBtn.setVisibility(View.VISIBLE);
		} catch (ClassNotFoundException e) {
			Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found");

		}
		ssoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * 下面两个注释掉的代码，仅当sdk支持sso时有效，
				 */
				mSsoHandler = new SsoHandler(MainActivity.this, mWeibo);
				mSsoHandler.authorize(new WeiboAuthDialogListener());
			}
		});
		cancelBtn = (Button) findViewById(R.id.apiCancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccessTokenKeeper.clear(MainActivity.this);
				authBtn.setVisibility(View.VISIBLE);
				ssoBtn.setVisibility(View.VISIBLE);
				cancelBtn.setVisibility(View.INVISIBLE);
				mText.setText("");
			}
		});

		mText = (TextView) findViewById(R.id.show);
		MainActivity.accessToken = AccessTokenKeeper.readAccessToken(this);
		if (MainActivity.accessToken.isSessionValid()) {
			Weibo.isWifi = Utility.isWifi(this);
			try {
				Class sso = Class.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
				if (apiBtn != null) {
					apiBtn.setVisibility(View.VISIBLE);
				}
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
			}
			authBtn.setVisibility(View.INVISIBLE);
			ssoBtn.setVisibility(View.INVISIBLE);
			cancelBtn.setVisibility(View.VISIBLE);
			String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date(MainActivity.accessToken.getExpiresTime()));
			mText.setText("access_token 仍在有效期内,无需再次登录: \naccess_token:" + MainActivity.accessToken.getToken() + "\n有效期：" + date);
		} else {
			mText.setText("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
		}
		try {
			MainActivity.this.getUserId(null);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Intent intent = MainActivity.this.getIntent();
		EditText editText = (EditText) findViewById(R.id.edit_message);
		Bundle b = intent.getExtras();
		if (b == null) {
			return;
		}
		Object[] lstName = b.keySet().toArray();
		for (int i = 0; i < lstName.length; i++) {
			String keyName = lstName[i].toString();
			Log.e(keyName, keyName);
			Log.e(keyName, String.valueOf(b.get(keyName)));
		}
		editText.setText(String.valueOf(b.get("android.intent.extra.TEXT")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	class DoubanAuthDialogListener implements DoubanAuthListener {

		@Override
		public void onComplete(Bundle values) {
			Toast.makeText(MainActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onDoubanException(DoubanException e) {
			Toast.makeText(getApplicationContext(),"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onError(DoubanDialogError e) {
			Toast.makeText(getApplicationContext(),"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",Toast.LENGTH_LONG).show();
			
		}
		
	}

	class WeiboAuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			MainActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (MainActivity.accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(MainActivity.accessToken.getExpiresTime()));
				mText.setText("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: " + expires_in + "\r\n有效期：" + date);
				try {
					Class sso = Class.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
					if (apiBtn != null) {
						apiBtn.setVisibility(View.VISIBLE);
					}
				} catch (ClassNotFoundException e) {
					Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
				}
				cancelBtn.setVisibility(View.VISIBLE);
				AccessTokenKeeper.keepAccessToken(MainActivity.this,accessToken);
				Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/**
		 * 下面两个注释掉的代码，仅当sdk支持sso时有效，
		 */
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	public void getDoubanMovie(View view) {
		mEditText = (EditText) findViewById(R.id.edit_movie_name);
		String str = mEditText.getEditableText().toString();
		if(str == null || "".equals(str)) {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, "Please enter movie name!",Toast.LENGTH_LONG).show();
				}
			});
			return;
		}
		MovieAPI api = new MovieAPI(); 
		api.getMovie(str, null, 0, 0, new RequestListener() {
			
			@Override
			public void onIOException(IOException e) {
				Log.i(TAG, e.toString());
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "getDoubanMovie IOException!!!",Toast.LENGTH_LONG).show();
					}
				});
			}
			
			@Override
			public void onError(WeiboException e) {
				Log.i(TAG, e.toString());
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "getDoubanMovie Error!!!",Toast.LENGTH_LONG).show();
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
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(MainActivity.this, "No result!",Toast.LENGTH_LONG).show();
							}
						});
						return;
					}
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							MainActivity.this.getMovieDetail(s);
							Looper.loop();
						}
					}).start();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void getMovieDetail(String s) {
		Intent intent = new Intent(this, MovieActivity.class);
	    intent.putExtra(MOVIE_MESSAGE, s);
	    startActivity(intent);
	}

	public void getFriendList(View view) throws JSONException {
		mText = (TextView) findViewById(R.id.myshow);
		FriendshipsAPI friendshipsAPI = new FriendshipsAPI(accessToken);
		SharedPreferences pref = MainActivity.this.getSharedPreferences(USERID, Context.MODE_APPEND);
		String userId = pref.getString("userId", "");
		Log.i(TAG, "I got userId  , OYeah!!!" + userId);
		if(userId == null || "".equals(userId)) {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, "You have not got userId yet!!!",Toast.LENGTH_LONG).show();
				}
			});
			return;
		}
		long uid = Long.valueOf(userId);
		friendshipsAPI.friends(uid, 20, 0, false, new RequestListener() {
			String myfriendList = "";
			@Override
			public void onIOException(IOException e) {
				Log.i(TAG, e.toString());
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "GETFRIENDLIST IOException!!!",Toast.LENGTH_LONG).show();
					}
				});
			}
			@Override
			public void onError(WeiboException e) {
				Log.i(TAG, e.toString());
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "GETFRIENDLIST Error!!!",Toast.LENGTH_LONG).show();
					}
				});
			}
			@Override
			public void onComplete(String response) {
				
				Log.i(TAG, response);
				try {
					JSONObject json = new JSONObject(response);
					JSONArray array = json.getJSONArray("users");
					myfriendList = "";
					for (int i = 0; i < array.length(); i++) {
						myfriendList += array.getJSONObject(i).getString("screen_name");
						myfriendList += "  ";
					}
//					String[] arrayItems = (String[])list.toArray(new String[size]);
					SharedPreferences pref = MainActivity.this.getSharedPreferences(FRIENDLIST, Context.MODE_APPEND);
					Editor editor = pref.edit();
					editor.putString(FRIENDLIST, array.toString());
					editor.commit();
					Log.i(TAG, myfriendList);
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "Success!!!",Toast.LENGTH_LONG).show();
//							mText.setText("You are following : " + myfriendList);
						}
					});
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							try {
								MainActivity.this.setAutoCompleteAdapter();
							} catch (JSONException e) {
								e.printStackTrace();
							}
							Looper.loop();
						}
					}).start();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		SharedPreferences p = MainActivity.this.getSharedPreferences(FRIENDLIST, Context.MODE_APPEND);
		String str = p.getString(FRIENDLIST, "");
		if(str == null || "".equals(str)) {
			return;
		}
		JSONArray array = new JSONArray(str);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			list.add(array.getJSONObject(i).getString("screen_name"));
		}
		int size = list.size();
		final String[] arrayItems = (String[])list.toArray(new String[size]);
		mAutoText = (AutoCompleteTextView)findViewById(R.id .editAuto);
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAutoText.setAdapter(new ArrayAdapter<String>(MainActivity.this ,android.R.layout.simple_dropdown_item_1line ,arrayItems));
			}
		});
		
	}
	public void addFriend(View view) {
		mEditText = (EditText) findViewById(R.id.edit_message);
		mAutoText = (AutoCompleteTextView)findViewById(R.id .editAuto);  
		String friendName = mAutoText.getEditableText().toString();
		if(friendName == null || "".equals(friendName)) {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, "ADD can not be null!!!",Toast.LENGTH_LONG).show();
				}
			});
			
			return ;
		}
 		friendName = "@" + friendName + " ";
		
		String str = mEditText.getEditableText().toString();
		str = str + friendName;
		final String s = str;
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mEditText.setText(s);
			}
		});
		mAutoText.getText().clear();
	
	}
	
	public void getUserId(View view) throws JSONException {
		
		/**
		 * 获取当前用户最新发表的微博列表
		 * 
		 * @param screen_name 需要查询的用户昵称。
		 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
		 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
		 * @param count 单页返回的记录条数，默认为50。
		 * @param page 返回结果的页码，默认为1。
		 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
		 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
		 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
		 * @param listener
		 */
		StatusesAPI statusesAPI = new StatusesAPI(accessToken);
		statusesAPI.userTimeline(0L, 0L, 1, 1, false, FEATURE.ALL, false, new RequestListener() {
			
			@Override
			public void onIOException(IOException e) {
				Log.i(TAG, e.toString());
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "userTimeline IOException!!!",Toast.LENGTH_LONG).show();
					}
				});
			}
			@Override
			public void onError(WeiboException e) {
				Log.i(TAG, e.toString());
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "userTimeline Error!!!",Toast.LENGTH_LONG).show();
					}
				});
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
					SharedPreferences pref = MainActivity.this.getSharedPreferences(USERID, Context.MODE_APPEND);
					Editor editor = pref.edit();
					editor.putString("userId", userId);
					editor.commit();
//					ToastRunnable t = new ToastRunnable(userId, new Runnable());
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							try {
								MainActivity.this.getFriendList(null);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							Looper.loop();
						}
					}).start();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	public void setAutoCompleteAdapter() throws JSONException {
		SharedPreferences p = MainActivity.this.getSharedPreferences(FRIENDLIST, Context.MODE_APPEND);
		String str = p.getString(FRIENDLIST, "");
		if(str == null || "".equals(str)) {
			return;
		}
		JSONArray array = new JSONArray(str);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			list.add(array.getJSONObject(i).getString("screen_name"));
		}
		int size = list.size();
		final String[] arrayItems = (String[])list.toArray(new String[size]);
		mAutoText = (AutoCompleteTextView)findViewById(R.id .editAuto);
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAutoText.setAdapter(new ArrayAdapter<String>(MainActivity.this ,android.R.layout.simple_dropdown_item_1line ,arrayItems));
			}
		});
	}

	public void sendMessage(View view) {
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		// 调用 新浪给的接口读取本地保存的token 有了token就可以去post query 微博了
		accessToken = AccessTokenKeeper.readAccessToken(this);
		// 调用微博接口 post
		StatusesAPI statusesAPI = new StatusesAPI(accessToken);
		statusesAPI.update("#自己的话#" + message, "0", "0", new RequestListener() {
			@Override
			public void onIOException(IOException e) {
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "IOException!!!",Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onError(WeiboException e) {
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "Error!!!",Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onComplete(final String response) {
				// post成功以后返回 成功
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "Post Success!!!",Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	public void share(View view) {
		// 通过ID取到EditText这个对象
		mEditText = (EditText) findViewById(R.id.edit_message);
		// getText拿到对象里面的值 string
		String subject = mEditText.getText().toString();
		String text = subject;
		/*
		 * Android的机制： 每次安装一个新的app的时候 app会向系统注册 我是谁我能干什么事儿 我可以提供哪些接口
		 * 然后这边这个方法就向系统请求 哪些app有ACTION_SEND这个功能，然后系统会把有这些功能的app返给我
		 * 就是点击share以后产生的界面 都是系统已经实现好的功能了
		 */
		Intent intent = new Intent(Intent.ACTION_SEND);
		// 这里传入的subject就是对应edittext里面的文本 作为点击以后新对话框的标题
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.setType("text/plain");
		// 这里传入的text就是对应edittext里面的文本 对应调用传入的参数
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 启动一个新的activity 就是看到的 成功调用别的app了
		startActivity(Intent.createChooser(intent, subject));
	}
}
