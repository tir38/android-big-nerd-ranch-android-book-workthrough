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
					if(mPlayer == null){
						Log.e(TAG, "I HAVE NO PLAYER");
					}
					mPlayer.play(getActivity());
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
