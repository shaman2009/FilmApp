package com.weibo.sdk.android.doubanapi;

import com.douban.sdk.android.MyParameters;
import com.douban.sdk.android.net.AsyncDoubanRunner;
import com.douban.sdk.android.net.DoubanRequestListener;


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
			final String httpMethod,DoubanRequestListener listener) {
//		params.add("access_token", accessToken);
		AsyncDoubanRunner.request(url, params, httpMethod, listener);
	}
}
