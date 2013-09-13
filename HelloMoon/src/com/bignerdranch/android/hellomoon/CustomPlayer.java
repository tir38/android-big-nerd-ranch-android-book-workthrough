package com.bignerdranch.android.hellomoon;

import android.R.bool;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;

public class CustomPlayer {
	
	// NOTE on pausing: I don't want to pause the MediaPlayer indefinitely.
	// So, I "pause" the AudioPlayer by pausing MediaPlayer, getting the current position of the MediaPlayer, 
	// save that position to AudioPlayer.mPosition, then release it. Then on "play" I just resume from mPosition, even if mPosition = 0 (beginning)
	
	// TODO: eventually I will have to preserve mPosition across instances (i.e during rotation, for now don't worry about it)
	
	private int mPosition  = 0; 					// instantiate to zero (the beginning)
	private MediaPlayer mPlayer;
	private int mResID; 							// resource ID for media to be played
	public static final String TAG = "helloMoon";
	
	// constructor
	public CustomPlayer(int resID){
		mResID = resID; 
	}
	
	public void play(Context context){
		// begin by releasing
		release();
		
		// create player
		mPlayer = MediaPlayer.create(context, mResID);
		
		// confirm create
		if (mPlayer == null){
			Log.w(TAG, "player not created");
			return;
		}
		
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
					player.start(); // start
				}
			}
		);
		
		// actually do stuff
		mPlayer.seekTo(mPosition);
	}
	
	public void stop(){
		release();	 	// release
		mPosition = 0;  // reset mPosition
	}
	
	public void pause(){
		if(mPlayer != null){							// null check
			mPlayer.pause();							// pause
			mPosition = mPlayer.getCurrentPosition(); 	// get current position
			release(); 									// release
		}
	}
	
	private void release(){
		if(mPlayer != null){							// null check
			mPlayer.release();							// release it
			mPlayer = null;								// and make null
		}
	}

	// only needed for video
	public void setCustomDisplay(SurfaceHolder surfaceHolder){
		if (mPlayer != null){ 						// null check
			mPlayer.setDisplay(surfaceHolder);		// set display via surface holder
		}
	}
}
