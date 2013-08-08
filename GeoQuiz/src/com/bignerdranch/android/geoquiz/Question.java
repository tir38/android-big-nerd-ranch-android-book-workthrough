package com.bignerdranch.android.geoquiz;

public class Question {

	private int mStringResID;
	private boolean mAnswer;
	
	public Question(int stringResID, boolean answer){ // constructor
		mStringResID = stringResID;
		mAnswer = answer;
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
}
