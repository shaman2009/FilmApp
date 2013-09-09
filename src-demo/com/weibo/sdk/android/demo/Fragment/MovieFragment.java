package com.weibo.sdk.android.demo.Fragment;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weibo.sdk.android.demo.R;

public class MovieFragment extends Fragment {
	
	private ArrayList<Data> mDatas = new ArrayList<Data>();
	private BaseAdapter mAdapter ;
	private int mHeight;
	private int mWidth;
	
	public void initData(){
		mDatas.clear();
		Data d = new Data();
		d.mName = "aaa"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "bbbb"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "cc"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "ddddd"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "ee"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "雷布斯"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "gg"; 
		mDatas.add(d);
		
		d = new Data();
		d.mName = "hh"; 
		mDatas.add(d);
		
		
		if(mAdapter!= null) {
			mAdapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
//			mDatas = (ArrayList<Data>) savedInstanceState.getSerializable("listViewData");
		}
		
		DisplayMetrics  dm = new DisplayMetrics();    
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);    
	    mWidth = dm.widthPixels;              
		mHeight = dm.heightPixels;  
		
		FrameLayout mFrame = new FrameLayout(getActivity());
		initData();
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		mFrame.setLayoutParams(lp);
		
		ListView mList = new ListView(getActivity());
		mAdapter = new BaseAdapter(){
			@Override
			public int getCount() {
				return mDatas.size();
			}
			@Override
			public Object getItem(int position) {
				return mDatas.get(position);
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder mHolder ; 
				if(convertView==null){
					mHolder = new ViewHolder();
					ItemLayout item = new ItemLayout(getActivity());
					mHolder.mText = (TextView)item.findViewWithTag(ItemLayout.TEXT_TAG);
					mHolder.mImage = (ImageView)item.findViewWithTag(ItemLayout.IMAGE_TAG);
					mHolder.mCheck = (CheckBox)item.findViewWithTag(ItemLayout.CHECK_TAG);
					convertView = item ;
					convertView.setTag(mHolder);
				}else{
					mHolder = (ViewHolder)convertView.getTag();
				}
				Data d = (Data)getItem(position);
				if(d == null) {
					return convertView ;
				}
				mHolder.mText.setText(d.mName);
				mHolder.mCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						
						
					}
				});
				return convertView;
			}
		};
		mList.setAdapter(mAdapter);
		mFrame.addView(mList);
		return mFrame ;
//		return inflater.inflate(R.layout.fragment_movie, container,false);
	}
	
	class Data {
		Drawable mImage ;
		String mName ;
	}
	
	class ViewHolder {
		TextView mText ;
		ImageView mImage ;
		CheckBox mCheck ; 
	}
	
	class ItemLayout extends FrameLayout{

		public static final String TEXT_TAG = "mcontext";
		public static final String IMAGE_TAG = "image";
		public static final String CHECK_TAG = "check";
		ImageView mImage ;
		TextView mContent ;
		CheckBox mCheck ;
		
		public ItemLayout(Context context) {
			super(context);
			init();
		}
		
		public void init(){
			addText(this);
			addImage();
			addCheck();
		}
		
		public void addImage(){
			mImage = new ImageView(getContext());
			LayoutParams lp = new LayoutParams(100,100);
			lp.gravity = Gravity.LEFT | Gravity.TOP ;
			mImage.setTag(IMAGE_TAG);
			mImage.setBackgroundResource(R.drawable.ic_launcher);
			addView(mImage,lp);
		}
		
		public void addCheck(){
			mCheck = new CheckBox(getActivity());
			LayoutParams lp = new LayoutParams(100,100);
			lp.gravity = Gravity.RIGHT | Gravity.TOP;
			mCheck.setTag(CHECK_TAG);
			addView(mCheck,lp);
		}
		
		public void addText(FrameLayout mF){
			mContent = new TextView(getContext());
			mContent.setTag(TEXT_TAG);
			mContent.setGravity(Gravity.CENTER);
			mContent.setTextSize(20);
			mContent.setText("hello");
			LayoutParams lp = new LayoutParams(mWidth - 100,100);
			lp.gravity = Gravity.LEFT | Gravity.TOP ;
			lp.leftMargin = 110 ;
			addView(mContent,lp);
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("listViewData", mDatas);
	}
}

