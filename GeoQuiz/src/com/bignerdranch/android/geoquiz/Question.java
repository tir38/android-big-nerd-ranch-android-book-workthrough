package com.bignerdranch.android.geoquiz;

public class Question {

	private int mStringResID;
	private boolean mAnswer;
	private boolean mWasCheater; // to store if question was cheated on
	
	public Question(int stringResID, boolean answer){ // constructor
		mStringResID = stringResID;
		mAnswer = answer;
		mWasCheater = false; // set default cheater status to false
	}

	public int getStringResID() {
		return mStringResID;
	}

	public void setStringResID(int stringResID) {
		mStringResID = stringResID;
	}

	public boolean getAnswer() {
		return mAnswer;
	}
	public void setAnswer(boolean answer) {
		mAnswer = answer;
	}
	
	public boolean getWasCheater(){
		return mWasCheater;
	}

	public void setWasCheater(boolean wasCheater){
		mWasCheater = wasCheater;
	}
}
