package com.bignerdranch.android.criminalintent;

import java.util.UUID;

public class Crime {
	private UUID mID;
	private String mTitle;
	
	public Crime(){ // constructor
		mID = UUID.randomUUID(); // generate random (unique?) ID
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
}
