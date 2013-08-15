package com.weibo.sdk.android.doubanapi;

import com.weibo.sdk.android.MyParameters;
import com.weibo.sdk.android.net.AsyncWeiboRunner;
import com.weibo.sdk.android.net.RequestListener;

public class DoubanAPI {
	/**
     * 访问douban服务接口的地址
     */
	public static final String API_SERVER = "https://api.douban.com/v2";
	/**
	 * post请求方式
	 */
	public static final String HTTPMETHOD_POST = "POST";
	/**
	 * get请求方式
	 */
	public static final String HTTPMETHOD_GET = "GET";
	
	
	
	protected void request( final String url, final MyParameters params,
			final String httpMethod,RequestListener listener) {
//		params.add("access_token", accessToken);
		AsyncWeiboRunner.request(url, params, httpMethod, listener);
	}
}
