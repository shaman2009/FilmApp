package com.weibo.sdk.android.demo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.weibo.sdk.android.demo.R;
import com.weibo.sdk.android.demo.adapter.PublicTimelineListAdapter;

public class PublicTimelineFragment extends Fragment{
	private ListView listView  = null;
	private PublicTimelineListAdapter adapter = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_public_timeline, container, false);
	}
	@Override
	public void onStart() {
		adapter = new PublicTimelineListAdapter(getActivity(), 0);
		initComponents();
		super.onStart();
	}
	
	private void initComponents() {
		listView = (ListView)getActivity().findViewById(R.id.lvMicroBlog);
		listView.setAdapter(adapter);
		listView.setFastScrollEnabled(true);
        
	}
}
