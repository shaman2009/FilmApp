package com.weibo.sdk.android.util;

public class ToastRunnable {
	private Object object;
	private Runnable runnable;
	public ToastRunnable(Object object, Runnable runnable) {
		this.object = object;
		this.runnable = runnable;
	}
}
