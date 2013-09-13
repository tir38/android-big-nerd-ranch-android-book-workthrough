package com.bignerdranch.android.hellomoon;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class HelloMoonFragment extends Fragment {

	private Button mPlayButton;
	private Button mPauseButton;
	private Button mStopButton;
	private AudioPlayer mPlayer = new AudioPlayer();
	public static final String TAG = "helloMoon";
	
	public View onCreateView(LayoutInflater inflater,
								ViewGroup parent,
								Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_hello_moon, parent, false);
		
		// handle play button
		mPlayButton = (Button)v.findViewById(R.id.hellomoon_playButton);
		mPlayButton.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View view) {
					Log.d(TAG, "about to call play");
					mPlayer.play(getActivity());
				}
			}
		);
		
		// handle pause button
		mPauseButton = (Button)v.findViewById(R.id.hellomoon_pauseButton);
		mPauseButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) {
					Log.d(TAG, "about to call pause");
					mPlayer.pause();
				}
			}
		);
		
		// handle stop button
		mStopButton = (Button)v.findViewById(R.id.hellomoon_stopButton);
		mStopButton.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View view) {
					Log.d(TAG, "about to call stop");
					mPlayer.stop();
				}
			}
		);
		
		return v;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy(); // call super class
		mPlayer.stop();
	}
}
