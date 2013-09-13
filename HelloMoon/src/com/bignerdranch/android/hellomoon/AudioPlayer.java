package com.bignerdranch.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioPlayer {
	
	// NOTE on pausing: I don't want to pause the MediaPlayer indefinitely.
	// So, I "pause" the AudioPlayer by pausing MediaPlayer, getting the current position of the MediaPlayer, 
	// save that position to AudioPlayer.mPosition. Then on "play" I either start or resume from mPosition.
	
	// TODO: eventually I will have to preserve mPosition across instances (i.e during rotation, for now don't worry about it)
	
	private int mPosition  = 0; // instantiate to zero (the beginning)
	private MediaPlayer mPlayer;
	public static final String TAG = "helloMoon";
	
	public void play(Context context){
		Log.d(TAG, "inside AudioPlayer.play()");
		release();
		
		mPlayer = MediaPlayer.create(context, R.raw.one_small_step);
		
		// setup  listeners
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
			{
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					stop();
				}
			}
		);
	
		mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() 
			{
				@Override
				public void onSeekComplete(MediaPlayer player) {
					Log.d(TAG, "actually about to start");
					player.start(); // start
				}
			}
		);
		
		// actually do stuff
		mPlayer.seekTo(mPosition);
	}
	
	public void stop(){
		Log.d(TAG, "inside AudioPlayer.stop()");
		release();	 	// release
		mPosition = 0;  // reset mPosition
	}
	
	public void pause(){
		Log.d(TAG, "inside AudioPlayer.pause()");
		if(mPlayer != null){							// null check
			mPlayer.pause();							// pause
			mPosition = mPlayer.getCurrentPosition(); 	// get current position
			release(); 									// release
		}
	}
	
	private void release(){
		Log.d(TAG, "inside AudioPlayer.release()");
		if(mPlayer != null){							// null check
			mPlayer.release();							// release it
			mPlayer = null;								// and make null
		}
	}
}
