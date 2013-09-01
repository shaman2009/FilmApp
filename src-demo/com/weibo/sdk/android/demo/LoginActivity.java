package com.weibo.sdk.android.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.douban.sdk.android.Douban;
import com.douban.sdk.android.DoubanAuthListener;
import com.douban.sdk.android.DoubanDialogError;
import com.douban.sdk.android.DoubanException;
import com.douban.sdk.android.net.DoubanRequestListener;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.doubanapi.AuthAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.sso.SsoHandler;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	
	//weibo auth
	private Weibo mWeibo;
	private Douban mDouban;
	private static final String WEIBO_CONSUMER_KEY = "2867503323";// 替换为开发者的appkey，例如"1646212860";
	private static final String WEIBO_REDIRECT_URL = "http://jianshu.io/users/SLfZEb";
	private static final String DOUBAN_CONSUMER_KEY = "0e32be84b39035752a983b8e1ab0a05f";
	private static final String DOUBAN_CLIENT_SECRET = "a560769d3d9b0dc7";
	private static final String DOUBAN_REDIRECT_URL = "http://shaman.logdown.com";
	private Button authBtn, apiBtn, ssoWeiboBtn, cancelBtn ,doubanAuthBtn;
	SsoHandler mSsoHandler;
	public static Oauth2AccessToken accessToken;
	public static final String SINA_SDK_TAG = "sinasdk";




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		mWeibo = Weibo.getInstance(WEIBO_CONSUMER_KEY, WEIBO_REDIRECT_URL);
		mDouban = Douban.getInstance(DOUBAN_CONSUMER_KEY, DOUBAN_CLIENT_SECRET, DOUBAN_REDIRECT_URL);
		ssoWeiboBtn = (Button)findViewById(R.id.sign_in_weibo);
		try {
			Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
		} catch (ClassNotFoundException e) {
			ssoWeiboBtn.setVisibility(View.INVISIBLE);
			Log.i(SINA_SDK_TAG, "com.weibo.sdk.android.sso.SsoHandler not found");
		}
		ssoWeiboBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler = new SsoHandler(LoginActivity.this, mWeibo);
				mSsoHandler.authorize(new WeiboAuthDialogListener());
			}
		});
		doubanAuthBtn = (Button)findViewById(R.id.sign_in_douban);
		doubanAuthBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDouban.authorize(LoginActivity.this, new DoubanAuthDialogListener());
			}
		});
		
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	class DoubanAuthDialogListener implements DoubanAuthListener {
		@Override
		public void onComplete(Bundle values) {
			Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();			
			List<String> list = new ArrayList<String>();
			Set<String> set = values.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()) {
				String key = it.next();
				list.add(key);
				list.add(values.getString(key));
			}
			Log.d("Douban-authorize",list.toString());
			AuthAPI authAPI = new AuthAPI();
			authAPI.getToken(DOUBAN_CONSUMER_KEY, DOUBAN_CLIENT_SECRET, DOUBAN_REDIRECT_URL, Douban.AUTHORIZATION_CODE, values.getString(Douban.KEY_CODE), new DoubanRequestListener() {
				@Override
				public void onIOException(IOException e) {
					Toast.makeText(getApplicationContext(),"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				@Override
				public void onError(DoubanException e) {
					Toast.makeText(getApplicationContext(),"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				@Override
				public void onComplete(String response) {
					Log.d("Douban-authorize",response);
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							Log.d(SINA_SDK_TAG, "call login...");
							LoginActivity.this.login();
							Looper.loop();
						}
					}).start();
					// get token via authorization_code 
//					{
//					    "access_token": "4257c1f43410c560c70745dc9c325020",
//					    "douban_user_name": "shaman",
//					    "douban_user_id": "55832962",
//					    "expires_in": 604800,
//					    "refresh_token": "5d3c793c5a7e080917b56bb50f7a181b"
//					}	
				}
			});
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
			Log.d(SINA_SDK_TAG, "WeiboAuthDialogListener onComplete");
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			LoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (LoginActivity.accessToken.isSessionValid()) {
				try {
					Class sso = Class.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
				} catch (ClassNotFoundException e) {
					Log.i(SINA_SDK_TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
				}
				AccessTokenKeeper.keepAccessToken(LoginActivity.this,accessToken);
				Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						Log.d(SINA_SDK_TAG, "call login...");
						LoginActivity.this.login();
						Looper.loop();
					}
				}).start();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Log.d(SINA_SDK_TAG, "WeiboAuthDialogListener onError");
			Toast.makeText(getApplicationContext(),"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Log.d(SINA_SDK_TAG, "WeiboAuthDialogListener onCancel");
			Toast.makeText(getApplicationContext(), "Auth cancel",Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.d(SINA_SDK_TAG, "WeiboAuthDialogListener onWeiboException");
			Toast.makeText(getApplicationContext(),"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	public void login() {
		Log.d(SINA_SDK_TAG, "login...");
		Intent intent = new Intent(this, SlidingmenuActivity.class);
	    startActivity(intent);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
