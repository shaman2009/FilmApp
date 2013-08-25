package com.weibo.sdk.android.doubanapi;

import com.douban.sdk.android.MyParameters;
import com.douban.sdk.android.net.DoubanRequestListener;


public class MovieAPI extends DoubanAPI{
	
	private static final String SERVER_URL_PRIX = API_SERVER + "/movie";

	public void getMovie(String q, String tag, int start, int count, DoubanRequestListener listener) {
		MyParameters params = new MyParameters();
		params.add("q", q);
		params.add("tag", tag);
//		params.add("start", start);
//		params.add("count", count);
		request( SERVER_URL_PRIX + "/search", params, HTTPMETHOD_GET, listener);
	}
}
