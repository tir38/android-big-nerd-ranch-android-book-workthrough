package com.bignerdranch.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioPlayer {
	private MediaPlayer mPlayer;
	public static final String TAG = "helloMoon";
	
	public void play(Context context){
		Log.d(TAG, "inside AudioPlayer.play()");
		stop();
		
		mPlayer = MediaPlayer.create(context, R.raw.one_small_step);
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
			{
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					stop();
				}
			}
		);
		
		mPlayer.start();
	}
	
	public void stop(){
		Log.d(TAG, "inside AudioPlayer.stop()");
		if(mPlayer != null){ 		// if not null
			mPlayer.release();		// release it
			mPlayer = null;			// and make null
		}
	}
	
	
}
