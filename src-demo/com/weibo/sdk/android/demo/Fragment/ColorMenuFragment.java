package com.weibo.sdk.android.demo.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weibo.sdk.android.demo.LoginActivity;
import com.weibo.sdk.android.demo.R;
import com.weibo.sdk.android.demo.SettingsActivity;
import com.weibo.sdk.android.demo.SlidingmenuActivity;
import com.weibo.sdk.android.demo.R.array;
import com.weibo.sdk.android.demo.R.color;
import com.weibo.sdk.android.demo.R.layout;

public class ColorMenuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] colors = getResources().getStringArray(R.array.color_names);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, colors);
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new PostFragment();
			break;
		case 1:
			newContent = new MovieFragment();
			break;
		case 2:
			newContent = new ColorFragment(R.color.blue);
			break;
		case 3:
			newContent = new ColorFragment(android.R.color.white);
			break;
		case 4:
			Intent intent = new Intent(getActivity(), SettingsActivity.class);
		    startActivity(intent);
			break;
		case 5:
			new AlertDialog.Builder(getActivity())
				.setTitle("注销")
				.setMessage("你是否确定要登出")
				.setNegativeButton("取消", null)
				.setNeutralButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					LoginActivity loginActivity = new LoginActivity();
					loginActivity.deleteloginsession(getActivity());
					Intent intent = new Intent(getActivity(), LoginActivity.class);
				    startActivity(intent);
				    getActivity().finish();
				}
			}).show();
			
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof SlidingmenuActivity) {
			SlidingmenuActivity fca = (SlidingmenuActivity) getActivity();
			fca.switchContent(fragment);
		} 
	}
}
