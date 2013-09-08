/**
 * Sep 2, 2013
 * AccountSettingFragment.java
 * @author fengxiang
 */
package com.weibo.sdk.android.demo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AccountSettingFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			
		}
		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		outState.putInt("mColorRes", mColorRes);
	}
}
