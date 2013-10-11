package com.weibo.sdk.android.demo.Fragment;

import java.util.ArrayList;

import com.filmapp.message.MesInfo;
import com.filmapp.message.MessageView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MessageFragment extends Fragment{

	public MessageFragment(){
		initTestData();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		if (savedInstanceState != null)
//			mColorRes = savedInstanceState.getInt("mColorRes");
//		int color = getResources().getColor(mColorRes);
//		// construct the RelativeLayout
		FrameLayout mFrame = new FrameLayout(getActivity());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
		ListView lv = new ListView(getActivity());
		lv.setLayoutParams(lp);
		lv.setAdapter(mMesAdapter);
		mFrame.addView(lv);
		return mFrame;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	BaseAdapter mMesAdapter = new BaseAdapter(){

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMessages.size();
		}

		@Override
		public MesInfo getItem(int position) {
			// TODO Auto-generated method stub
			return mMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MessageView.ViewHolder mh ;
			if(convertView == null ){
				mh = new MessageView.ViewHolder();
				MessageView mv = new MessageView(parent.getContext());
				mh.mHeadIconView = (ImageView)mv.findViewWithTag(MessageView.ICON_TAG);
				mh.mContentView = (View)mv.findViewWithTag(MessageView.CONTENT_TAG);
				mh.mTitle = (TextView)mv.findViewWithTag(MessageView.TITLE_TAG);
				mh.mScriptText = (TextView)mv.findViewWithTag(MessageView.SCRIPT_TAG);
				convertView = mv ;
				convertView.setTag(mh);
			}else{
				mh = (MessageView.ViewHolder)convertView.getTag();
			}
			MesInfo mi = mMessages.get(position);
			if(mi==null){
				return convertView ;
			}
			mh.mTitle.setText(mi.mMesTitle);
			mh.mScriptText.setText(mi.mScriptDes);
			return convertView;
		}
		
	};
	
	ArrayList<MesInfo> mMessages = new ArrayList<MesInfo>();
	
	//TODO it is test data
	public void initTestData(){
		mMessages.clear();
		for(int i=0;i<50;i++){
			MesInfo mi = new MesInfo();
			mi.mIconUrl = "icon"+i ;
			mi.mContentDes = " i send this message for " + i ;
			mi.mContentUrl = "content url " + i ;
			mi.mMesTitle = "message title "+i;
			mi.mScriptDes = "message srcipt "+i;
			mMessages.add(mi);
		}
	}
}
