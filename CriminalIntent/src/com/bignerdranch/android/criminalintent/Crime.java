package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
	private UUID mID;
	private String mTitle;
	private Date mDate; // date crime occurred
	private boolean mSolved; // 
	
	public Crime(){ // constructor
		mID = UUID.randomUUID(); // generate random (unique?) ID
		mDate = new Date(); // defaults to current date
	}

	// getters and setters
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getID() {
		return mID;
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean isSolved) {
		mSolved = isSolved;
	}
	
	@Override
	public String toString(){
		return mTitle;
	}
}
