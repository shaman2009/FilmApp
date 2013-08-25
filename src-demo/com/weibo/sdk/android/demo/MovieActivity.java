package com.weibo.sdk.android.demo;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.weibo.sdk.android.util.FileManager;
import com.weibo.sdk.android.util.GetImage;

public class MovieActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	public final static String MOVIE_MESSAGE = "douban.movie.json";
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MovieActivity.this.setContentView(R.layout.activity_movie);

		// Get the message from the intent
	    Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.MOVIE_MESSAGE);
	    
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),message);
		

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		String message;
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public SectionsPagerAdapter(FragmentManager fm, String message) {
			super(fm);
			this.message = message;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putString(DummySectionFragment.MESSAGE, message);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			JSONObject movieResult;
			int i = 0;
			if(message == null || "".equals(message)) {
				return 1;
			}
			try {
				movieResult = new JSONObject(message);
				i = Integer.valueOf(movieResult.getString("total"));;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(i > 5) {
				return 5;
			}else if(i > 0 && i < 5){
				return i;
			}else {
				return 1;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			JSONObject movieResult;
			JSONArray movies = new JSONArray();
			try {
				movieResult = new JSONObject(message);
				int i = Integer.valueOf(movieResult.getString("total"));;
				if(i > 3) {
					movies = movieResult.getJSONArray("subjects");//(getArguments().getInt(ARG_SECTION_NUMBER)).getString("title");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
//				return getString(R.string.title_section1).toUpperCase(l);
				try {
					return movies.getJSONObject(0).getString("title");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			case 1:
				try {
					return movies.getJSONObject(1).getString("title");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			case 2:
				try {
					return movies.getJSONObject(2).getString("title");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			case 3:
				try {
					return movies.getJSONObject(3).getString("title");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			case 4:
				try {
					return movies.getJSONObject(4).getString("title");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
	public void getMovieShare(String s) {
		Intent intent = new Intent(this, ShareMovieActivity.class);
	    intent.putExtra(MOVIE_MESSAGE, s);
	    startActivity(intent);
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String MESSAGE = "message";

		public DummySectionFragment() {
		}
		

		@Override
		public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//			container.getContext();
			String message = getArguments().getString(MESSAGE);
			Log.i("DummySectionFragment ", message);
			String movieTitle = "no result";
			String movieImageUrl = "";
			JSONObject movie = new JSONObject();
			final int count = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
			try {
				JSONObject movieResults = new JSONObject(message);
				int i = Integer.valueOf(movieResults.getString("total"));
				
				if(i > 5) {
					movie = movieResults.getJSONArray("subjects").getJSONObject(count);
					movieImageUrl = movie.getJSONObject("images").getString("large");
					movie.put("picPath", ((MovieActivity)container.getContext()).getFilesDir() + "/filmApp/" + count + ".jpg");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			final JSONObject j = movie;
			View rootView = inflater.inflate(R.layout.fragment_movie_dummy,container, false);
//			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)) + 10);
//			dummyTextView.setText(movieTitle);
			final ImageView dummyImageView = (ImageView) rootView.findViewById(R.id.imageView);
			Button btn = (Button) rootView.findViewById(R.id.buttonShare);	
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((MovieActivity)container.getContext()).getMovieShare(j.toString());
				}
			});
			AsyncTask<String, Void, Bitmap> asyncTask = new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... params) {
					Bitmap bitmap = GetImage.getHttpBitmap(params[0]);
					try {
						FileManager.saveBitmapToFile(bitmap, j.getString("picPath"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return bitmap;
				}
				@Override
				protected void onPostExecute(Bitmap result) {
					dummyImageView.setImageBitmap(result);
					super.onPostExecute(result);
				}
			};
			asyncTask.execute(movieImageUrl);
			return rootView;
		}
	}

}
