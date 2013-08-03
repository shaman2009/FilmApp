package com.weibo.sdk.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GetImage {
	public static HttpResponse doHttpRequst(HttpClient httpClient,
			HttpUriRequest httpPost) throws ClientProtocolException,
			IOException {
		return httpClient.execute(httpPost);
	}

	public static Bitmap loadBitmap(String url, String referer) {
		Bitmap bm = null;
		InputStream is = null;
		try {
			is = GetImage.getInputStream(url, referer);
			bm = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
				is = null;
			}
		}
		Log.i("pic", url);
		return bm;
	}
	
	public static InputStream getInputStream(String url, String referer) {
		if (url == null)
			return null;
		try {
			HttpGet httpGet = new HttpGet(url);
			if (referer != null) {
				httpGet.addHeader("referer", referer);
			}
			if (httpGet.getURI().getHost() == null)
				return null;
			HttpResponse httpResponse = GetImage.doHttpRequst(
					new DefaultHttpClient(), httpGet);
			if (httpResponse.getEntity() != null) {
				StatusLine statusLine = httpResponse.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					httpResponse.getEntity().writeTo(out);
					out.close();
					byte[] image_bytes = out.toByteArray();
					return new ByteArrayInputStream(image_bytes);
				} else {
					httpResponse.getEntity().consumeContent();
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	  public static Bitmap getHttpBitmap(String url){
	    	URL myFileURL;
	    	Bitmap bitmap=null;
	    	try{
	    		myFileURL = new URL(url);
	    		//获得连接
	    		HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
	    		//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
	    		conn.setConnectTimeout(6000);
	    		//连接设置获得数据流
	    		conn.setDoInput(true);
	    		//不使用缓存
	    		conn.setUseCaches(false);
	    		//这句可有可无，没有影响
	    		//conn.connect();
	    		//得到数据流
	    		InputStream is = conn.getInputStream();
	    		//解析得到图片
	    		Log.i("bigmap",is.toString());
	    		bitmap = BitmapFactory.decodeStream(is);
	    		//关闭数据流
	    		is.close();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	
			return bitmap;
	    	
	    }
}
