package com.bignerdranch.android.hellomoon;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class HelloMoonFragment extends Fragment {

	private SurfaceView mSurfaceView;
	
	private Button mPlayVideoButton;
	private Button mPauseVideoButton;
	private Button mStopVideoButton;
	private Button mPlayAudioButton;
	private Button mPauseAudioButton;
	private Button mStopAudioButton;
	
	private CustomPlayer mVideoPlayer = new CustomPlayer(R.raw.apollo_17_stroll);
	private CustomPlayer mAudioPlayer = new CustomPlayer(R.raw.one_small_step);
	public static final String TAG = "helloMoon";
	
	
	public View onCreateView(LayoutInflater inflater,
								ViewGroup parent,
								Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_hello_moon, parent, false);
		
		// ================ video surface view ===============
		mSurfaceView = (SurfaceView)v.findViewById(R.id.helloMoon_videoSurfaceView);
		if (mSurfaceView == null){
			Log.w(TAG, "I have no surface view");
		}
		mVideoPlayer.setCustomDisplay(mSurfaceView.getHolder()); // set videoplayer's surface
		
		// =============== video buttons ====================
		// handle play button 
		mPlayVideoButton = (Button)v.findViewById(R.id.hellomoon_playVideoButton);
		mPlayVideoButton.setOnClickListener(new View.OnClickListener() 	{
				@Override
				public void onClick(View view) {
					mVideoPlayer.play(getActivity());
				}
			}
		);
		
		// handle pause button
		mPauseVideoButton = (Button)v.findViewById(R.id.hellomoon_pauseVideoButton);
		mPauseVideoButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) {
					mVideoPlayer.pause();
				}
			}
		);
		
		// handle stop button
		mStopVideoButton = (Button)v.findViewById(R.id.hellomoon_stopVideoButton);
		mStopVideoButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View view) {
				Log.d(TAG, "about to call stop");
				mVideoPlayer.stop();
			}
		}
	);
				
				
		// =============== audio buttons ====================
		// handle play button
		mPlayAudioButton = (Button)v.findViewById(R.id.hellomoon_playAudioButton);
		mPlayAudioButton.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View view) {
					Log.d(TAG, "about to call play");
					mAudioPlayer.play(getActivity());
				}
			}
		);
		
		// handle pause button
		mPauseAudioButton = (Button)v.findViewById(R.id.hellomoon_pauseAudioButton);
		mPauseAudioButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) {
					Log.d(TAG, "about to call pause");
					mAudioPlayer.pause();
				}
			}
		);
		
		// handle stop button
		mStopAudioButton = (Button)v.findViewById(R.id.hellomoon_stopAudioButton);
		mStopAudioButton.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View view) {
					Log.d(TAG, "about to call stop");
					mAudioPlayer.stop();
				}
			}
		);
		
		return v;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy(); // call super class
		mAudioPlayer.stop();
	}
}
