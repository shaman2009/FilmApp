package com.weibo.sdk.android.doubanapi;

import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.RequestListener;

public class MovieAPI extends DoubanAPI{
	
	private static final String SERVER_URL_PRIX = API_SERVER + "/movie";

	public void getMovie( String q, String tag, int start, int count, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("tag", tag);
//		params.add("start", start);
//		params.add("count", count);
		request( SERVER_URL_PRIX + "/search", params, HTTPMETHOD_GET, listener);
	}
}
