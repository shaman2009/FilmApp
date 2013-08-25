/**
 * Aug 24, 2013
 * AuthAPI.java
 * @author fengxiang
 */
package com.weibo.sdk.android.doubanapi;

import com.douban.sdk.android.MyParameters;
import com.douban.sdk.android.net.DoubanRequestListener;

public class AuthAPI extends DoubanAPI{
	private static final String SERVER_URL_PRIX = "https://www.douban.com/service/auth2/";
	private static final String ACCESS_TOKEN_PATH = SERVER_URL_PRIX + "token";
	
	/**
	 * get access_token
	 */
	public void getToken( String client_id, String client_secret, String redirect_uri, String grant_type, String code, DoubanRequestListener listener) {
		MyParameters params = new MyParameters();
		params.add("client_id", client_id);
		params.add("client_secret", client_secret);
		params.add("redirect_uri", redirect_uri);
		params.add("grant_type", grant_type);
		params.add("code", code);
		request( ACCESS_TOKEN_PATH, params, HTTPMETHOD_POST, listener);
	}


}
